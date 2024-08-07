package com.example.weatherforecastapp.presentation.favorite

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
import javax.inject.Inject

class DefaultFavoriteComponent @AssistedInject constructor(
    private val storeFactory: FavoriteStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("onCityClicked") private val onCityClicked: (City) -> Unit,
    @Assisted("onAddFavoriteClicked") private val onAddFavoriteClicked: () -> Unit,
    @Assisted("onSearchClicked") private val onSearchClicked: () -> Unit
) : FavoriteComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentContext.componentScope()

    init {

        scope.launch {
            store.labels.collect {
                when (it) {
                    FavoriteStore.Label.ClickSearch -> {
                        onSearchClicked()
                    }

                    FavoriteStore.Label.ClickToFavorite -> {
                        onAddFavoriteClicked()
                    }

                    is FavoriteStore.Label.CityItemClicked -> {
                        onCityClicked(it.city)
                    }
                }
            }
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<FavoriteStore.State> = store.stateFlow

    override fun onClickSearch() {
        store.accept(FavoriteStore.Intent.ClickSearch)
    }

    override fun onClickAddFavorite() {
        store.accept(FavoriteStore.Intent.ClickAddToFavorite)

    }

    override fun onCityItemClick(city: City) {
        store.accept(FavoriteStore.Intent.CityItemClicked(city))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("onCityClicked") onCityClicked: (City) -> Unit,
            @Assisted("onAddFavoriteClicked") onAddFavoriteClicked: () -> Unit,
            @Assisted("onSearchClicked") onSearchClicked: () -> Unit
        ): DefaultFavoriteComponent
    }


}