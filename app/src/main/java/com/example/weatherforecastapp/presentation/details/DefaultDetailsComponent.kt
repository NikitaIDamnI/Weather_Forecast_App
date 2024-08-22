package com.example.weatherforecastapp.presentation.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.weatherforecastapp.domane.entity.City
import com.example.weatherforecastapp.presentation.extensions.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultDetailsComponent @AssistedInject constructor(
    private val storeFactory: DetailsStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("city") private val cities: List<City>,
    @Assisted("indexCity") indexCity: Int,
    @Assisted("onBackClick") private val onBackClick: () -> Unit,
) : DetailsComponent, ComponentContext by componentContext {


    private val store = instanceKeeper.getStore {
        storeFactory.create(
            indexCity = indexCity,
            city = cities
        )
    }
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
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("city") cities: List<City>,
            @Assisted("indexCity") indexCity: Int,
            @Assisted("onBackClick") onBackClick: () -> Unit,
        ): DefaultDetailsComponent
    }


}