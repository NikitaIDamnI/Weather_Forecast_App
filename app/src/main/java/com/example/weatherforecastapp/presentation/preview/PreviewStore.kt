package com.example.weatherforecastapp.presentation.preview

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.weatherforecastapp.domane.entity.City
import com.example.weatherforecastapp.domane.entity.Forecast
import com.example.weatherforecastapp.presentation.favorite.FavoriteStore.State.WeatherState
import com.example.weatherforecastapp.presentation.preview.PreviewStore.Intent
import com.example.weatherforecastapp.presentation.preview.PreviewStore.Label
import com.example.weatherforecastapp.presentation.preview.PreviewStore.State
import javax.inject.Inject

interface PreviewStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class AddToFavorite(
            val city: City
        ) : Intent

        data object BackFromPreview : Intent
    }

    data class State(
        val previewCity: PreviewCity
    ) {
        data class PreviewCity(
            val city: City,
            val forecast: Forecast,
            val isFavorite: Boolean
        )
    }

    sealed interface Label {
        data class AddToFavorite(
            val city: City
        ) : Label

        data object BackFromPreview : Label
    }
}

class PreviewStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory
) {

    fun create(
        city: City,
        forecast: Forecast,
        isFavorite: Boolean
    ): PreviewStore =
        object : PreviewStore, Store<Intent, State, Label> by storeFactory.create(
            name = "PreviewStore",
            initialState = State(
                previewCity = State.PreviewCity(
                    city = city,
                    forecast = forecast,
                    isFavorite = isFavorite
                )
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl(State.PreviewCity(
                city = city,
                forecast = forecast,
                isFavorite = isFavorite
            ))
        ) {}

    private sealed interface Action {}

    private sealed interface Msg {}

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.AddToFavorite -> {
                    publish(
                        Label.AddToFavorite(
                            city = intent.city
                        )
                    )
                }

                Intent.BackFromPreview -> {
                    publish(Label.BackFromPreview)
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
        }
    }

    private class ReducerImpl(
        val state : State.PreviewCity
    ) : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State= State(state)

    }
}
