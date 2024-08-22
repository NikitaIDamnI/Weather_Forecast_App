package com.example.weatherforecastapp.presentation.search

import android.util.Log
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.weatherforecastapp.domane.entity.City
import com.example.weatherforecastapp.domane.entity.Forecast
import com.example.weatherforecastapp.domane.entity.SearchCity
import com.example.weatherforecastapp.domane.usecase.ChangeFavoriteStateUseCase
import com.example.weatherforecastapp.domane.usecase.CheckFavoriteCitiesUseCase
import com.example.weatherforecastapp.domane.usecase.GetForecastWeatherUseCase
import com.example.weatherforecastapp.domane.usecase.SearchCityUseCase
import com.example.weatherforecastapp.presentation.search.SearchStore.Intent
import com.example.weatherforecastapp.presentation.search.SearchStore.Label
import com.example.weatherforecastapp.presentation.search.SearchStore.State
import com.example.weatherforecastapp.presentation.search.SearchStore.State.PreviewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

interface SearchStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class ChangeSearchQuery(val query: String) : Intent
        data object ClickSearch : Intent
        data object ClickBack : Intent
        data class ClickCity(val searchCity: SearchCity) : Intent
        data class ClickAddFavorite(val city: City) : Intent
        data object ClickClosePreview : Intent
    }

    data class State(
        val searchQuery: String,
        val searchState: SearchState,
        val previewState: PreviewState
    ) {
        sealed interface SearchState {
            data object Initial : SearchState
            data object Loading : SearchState
            data object Error : SearchState
            data class Loaded(val cities: List<SearchCity>) : SearchState
            data object EmptyResult : SearchState
        }

        sealed interface PreviewState {
            data object Initial : PreviewState
            data object Loading : PreviewState
            data class Loaded(
                val city: City,
                val forecast: Forecast,
                val isFavorite: Boolean
            ) : PreviewState

            data object Error : PreviewState
        }
    }

    sealed interface Label {
        data object ClickBack : Label
        data object ClickAddFavorite : Label
    }
}

class SearchStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val searchCitiesUseCase: SearchCityUseCase,
    private val changeFavoriteStateUseCase: ChangeFavoriteStateUseCase,
    private val checkFavoriteCitiesUseCase: CheckFavoriteCitiesUseCase,
    private val getForecastWeatherUseCase: GetForecastWeatherUseCase
) {

    fun create(): SearchStore =
        object : SearchStore, Store<Intent, State, Label> by storeFactory.create(
            name = "SearchStore",
            initialState = State(
                searchQuery = "",
                searchState = State.SearchState.Initial,
                previewState = State.PreviewState.Initial
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = { ExecutorImpl() },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {
        data class ChangeSearchQuery(val query: String) : Msg
        data object LoadingSearchResult : Msg
        data object SearchResultError : Msg
        data class SearchResultLoaded(val cities: List<SearchCity>) : Msg
        data object LoadingPreview : Msg
        data class LoadedPreview(
            val city: City,
            val forecast: Forecast,
            val isFavorite: Boolean
        ) : Msg

        data object LoadingPreviewError : Msg
        data object ClosePreview : Msg

    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl() :
        CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        private var searchJob: Job? = null
        private var loadPreviewJob: Job? = null

        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.ClickBack -> {
                    publish(Label.ClickBack)
                }

                Intent.ClickSearch -> {
                    dispatch(Msg.LoadingSearchResult)
                    searchJob?.cancel()
                    searchJob = scope.launch {
                        try {
                            val searchQuery = getState().searchQuery
                            Log.d("Search_tag", "executeIntent: $searchQuery")
                            val cities = searchCitiesUseCase(searchQuery)
                            dispatch(Msg.SearchResultLoaded(cities))

                        } catch (e: Exception) {
                            dispatch(Msg.SearchResultError)
                        }
                    }
                }

                is Intent.ChangeSearchQuery -> {
                    dispatch(Msg.ChangeSearchQuery(intent.query))
                }

                is Intent.ClickCity -> {
                    loadPreviewJob?.cancel()
                    dispatch(Msg.LoadingPreview)
                    loadPreviewJob = scope.launch {
                        try {
                            val forecast = getForecastWeatherUseCase(intent.searchCity.id)
                            val isFavorite = checkFavoriteCitiesUseCase(intent.searchCity.id)
                            dispatch(
                                Msg.LoadedPreview(
                                    city = City(
                                        id = intent.searchCity.id,
                                        name = intent.searchCity.name,
                                        country = intent.searchCity.country,
                                        weather = forecast.currentWeather
                                    ),
                                    forecast = forecast,
                                    isFavorite = isFavorite
                                )
                            )

                        } catch (_: RuntimeException) {
                            dispatch(Msg.LoadingPreviewError)
                        }

                    }
                }

                is Intent.ClickAddFavorite -> {
                    scope.launch {
                        changeFavoriteStateUseCase.addFavorite(intent.city)
                        publish(Label.ClickAddFavorite)
                    }
                }

                Intent.ClickClosePreview -> {
                    dispatch(Msg.ClosePreview)
                }

            }

        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.ChangeSearchQuery -> {
                copy(searchQuery = msg.query)
            }

            Msg.LoadingSearchResult -> {
                copy(searchState = State.SearchState.Loading)
            }

            Msg.SearchResultError -> {
                copy(searchState = State.SearchState.Error)
            }

            is Msg.SearchResultLoaded -> {
                if (msg.cities.isEmpty()) {
                    copy(searchState = State.SearchState.EmptyResult)
                } else {
                    copy(searchState = State.SearchState.Loaded(msg.cities))
                }
            }

            Msg.LoadingPreview -> {
                copy(previewState = PreviewState.Loading)

            }

            is Msg.LoadedPreview -> {
                copy(
                    previewState = PreviewState.Loaded(
                        msg.city,
                        msg.forecast,
                        msg.isFavorite
                    )
                )
            }

            Msg.LoadingPreviewError -> {
                copy(previewState = PreviewState.Error)
            }

            Msg.ClosePreview -> {
                copy(previewState = PreviewState.Initial)
            }
        }
    }
}
