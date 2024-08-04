package com.example.weatherforecastapp.presentation.preview

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.weatherforecastapp.presentation.preview.PreviewStore.Intent
import com.example.weatherforecastapp.presentation.preview.PreviewStore.Label
import com.example.weatherforecastapp.presentation.preview.PreviewStore.State

internal interface PreviewStore : Store<Intent, State, Label> {

    sealed interface Intent {
    }

    data class State(val todo: Unit = Unit)

    sealed interface Label {
    }
}

internal class PreviewStoreFactory(
    private val storeFactory: StoreFactory
) {

    fun create(): PreviewStore =
        object : PreviewStore, Store<Intent, State, Label> by storeFactory.create(
            name = "PreviewStore",
            initialState = State(),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
    }

    private sealed interface Msg {
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
        }

        override fun executeAction(action: Action, getState: () -> State) {
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = State()

    }
}
