package com.example.weatherforecastapp.presentation.details

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.weatherforecastapp.domane.entity.City
import com.example.weatherforecastapp.domane.entity.Forecast
import com.example.weatherforecastapp.domane.usecase.ChangeFavoriteStateUseCase
import com.example.weatherforecastapp.domane.usecase.GetForecastWeatherUseCase
import com.example.weatherforecastapp.domane.usecase.ObserveFavoriteStateUseCase
import com.example.weatherforecastapp.presentation.details.DetailsStore.Intent
import com.example.weatherforecastapp.presentation.details.DetailsStore.Label
import com.example.weatherforecastapp.presentation.details.DetailsStore.State
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

interface DetailsStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object ClickBack : Intent
    }

    data class State(
        val indexCity: Int,
        val city: List<City>,
        val isFavorite: Boolean,
        val forecastState: ForecastState
    ) {
        sealed interface ForecastState {
            data object Initial : ForecastState
            data object Loading : ForecastState
            data object Error : ForecastState
            data class Loaded(
                val forecast: List<Forecast>
            ) : ForecastState
        }
    }

    sealed interface Label {
        data object ClickBack : Label
    }
}

class DetailsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getForecastWeatherUseCase: GetForecastWeatherUseCase,
) {

    fun create(indexCity: Int, city: List<City>): DetailsStore =
        object : DetailsStore, Store<Intent, State, Label> by storeFactory.create(
            name = "DetailsStore",
            initialState = State(
                indexCity = indexCity,
                city = city,
                isFavorite = false,
                forecastState = State.ForecastState.Initial
            ),
            bootstrapper = BootstrapperImpl(city),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data class ForecastLoaded(val forecast: List<Forecast>) : Action
        data object ForecastIsLoading : Action
        data object ForecastLoadingError : Action
    }

    private sealed interface Msg {
        data class ForecastLoaded(val forecast: List<Forecast>) : Msg
        data class FavoriteStateChanged(val isFavorite: Boolean) : Msg
        data object ForecastIsLoading : Msg
        data object ForecastLoadingError : Msg
    }

    private inner class BootstrapperImpl(
        private val cities: List<City>
    ) : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            dispatch(Action.ForecastIsLoading)

            scope.launch {
                val forecasts = cities.map { city ->
                    async {
                        try {
                            getForecastWeatherUseCase(city.id)
                        } catch (e: Exception) {
                            dispatch(Action.ForecastLoadingError)
                            null
                        }
                    }
                }.awaitAll().filterNotNull()
                dispatch(Action.ForecastLoaded(forecasts))
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.ClickBack -> publish(Label.ClickBack)
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.ForecastLoaded -> {
                    dispatch(Msg.ForecastLoaded(action.forecast))
                }
                Action.ForecastIsLoading -> {
                    dispatch(Msg.ForecastIsLoading)
                }
                Action.ForecastLoadingError -> {
                    dispatch(Msg.ForecastLoadingError)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.FavoriteStateChanged -> {
                copy(isFavorite = msg.isFavorite)
            }
            Msg.ForecastIsLoading -> {
                copy(forecastState = State.ForecastState.Loading)
            }
            is Msg.ForecastLoaded -> {
                copy(forecastState = State.ForecastState.Loaded(forecast = msg.forecast))
            }
            Msg.ForecastLoadingError -> {
                copy(forecastState = State.ForecastState.Error)
            }
        }
    }
}
