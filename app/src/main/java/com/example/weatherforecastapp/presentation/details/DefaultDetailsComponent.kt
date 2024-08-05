package com.example.weatherforecastapp.presentation.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.weatherforecastapp.domane.entity.City
import com.example.weatherforecastapp.presentation.extensions.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefaultDetailsComponent @Inject constructor(
    componentContext: ComponentContext,
    private val storeFactory: DetailsStoreFactory,
    private val city: City,
    private val onBackClick: () -> Unit,
) : DetailsComponent, ComponentContext by componentContext {


    private val store = instanceKeeper.getStore { storeFactory.create(city) }
    private val scope = componentContext.componentScope()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<DetailsStore.State> = store.stateFlow

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    DetailsStore.Label.ClickBack -> {
                        onBackClick()
                    }
                }
            }
        }
    }


    override fun onClickBack() {
        store.accept(DetailsStore.Intent.ClickBack)
    }

    override fun onClickFavoriteStatus() {
        store.accept(DetailsStore.Intent.ClickChangeFavoriteStatus)
    }

}