package com.example.weatherforecastapp.presentation.search

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.weatherforecastapp.domane.entity.City
import com.example.weatherforecastapp.domane.entity.SearchCity
import com.example.weatherforecastapp.domane.usecase.ChangeFavoriteStateUseCase
import com.example.weatherforecastapp.domane.usecase.SearchCityUseCase
import com.example.weatherforecastapp.presentation.search.SearchStore.Intent
import com.example.weatherforecastapp.presentation.search.SearchStore.Label
import com.example.weatherforecastapp.presentation.search.SearchStore.State
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

interface SearchStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class SearchQueryChanged(val query: String) : Intent
        data object ClickSearch : Intent
        data object ClickBack : Intent
        data class ClickCity(val city: SearchCity) : Intent
    }

    data class State(
        val searchQuery: String,
        val searchState: SearchState
    ) {
        sealed interface SearchState {
            data object Initial : SearchState
            data object Loading : SearchState
            data object Error : SearchState
            data class Loaded(val cities: List<SearchCity>) : SearchState
            data object EmptyResult : SearchState
        }
    }

    sealed interface Label {
        data object ClickBack : Label
        data object SavedToFavorite : Label
        data class OpenForecast(val city: SearchCity) : Label
    }
}

class SearchStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val searchCitiesUseCase: SearchCityUseCase,
    private val changeFavoriteStateUseCase: ChangeFavoriteStateUseCase
) {

    fun create(openReason: OpenReason): SearchStore =
        object : SearchStore, Store<Intent, State, Label> by storeFactory.create(
            name = "SearchStore",
            initialState = State(
                searchQuery = "",
                searchState = State.SearchState.Initial
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = { ExecutorImpl(openReason) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {
        data class ChangeSearchQuery(val query: String) : Msg
        data object LoadingSearchResult : Msg
        data object SearchResultError : Msg
        data class SearchResultLoaded(val cities: List<SearchCity>) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl(private val openReason: OpenReason) :
        CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        private var searchJob: Job? = null

        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.ClickBack -> {
                    publish(Label.ClickBack)
                }

                Intent.ClickSearch -> {
                    dispatch(Msg.LoadingSearchResult)
                    try {
                        searchJob?.cancel()
                        searchJob = scope.launch {
                            val cities = searchCitiesUseCase(getState().searchQuery)
                            dispatch(Msg.SearchResultLoaded(cities))
                        }
                    } catch (e: Exception) {
                        dispatch(Msg.SearchResultError)
                    }


                }

                is Intent.SearchQueryChanged -> {
                    dispatch(Msg.ChangeSearchQuery(intent.query))
                }

                is Intent.ClickCity -> {
                    when (openReason) {
                        OpenReason.AddToFavorite -> {
                            scope.launch {
                                changeFavoriteStateUseCase.addFavorite(
                                    City(
                                        id = intent.city.id,
                                        name = intent.city.name,
                                        country = intent.city.country,
                                    )
                                )
                                publish(Label.SavedToFavorite)
                            }
                        }

                        OpenReason.RegularSearch -> {
                            publish(Label.OpenForecast(intent.city))
                        }
                    }
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
        }
    }
}
