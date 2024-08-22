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

class DefaultFavoriteComponent @AssistedInject constructor(
    private val storeFactory: FavoriteStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("onCityClicked") private val onCityClicked: (Int,List<City>) -> Unit,
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



                    is FavoriteStore.Label.CityItemClicked -> {
                        onCityClicked(
                            it.indexCity,
                            it.cities
                        )
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



    override fun onCityItemClick(indexCity: Int, cities: List<City>) {
        store.accept(FavoriteStore.Intent.CityItemClicked(indexCity,cities))
    }

    override fun onDeleteCity(cityId: Int) {
        store.accept(FavoriteStore.Intent.DeleteCity(cityId))
    }


    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("onCityClicked") onCityClicked: (Int,List<City>) -> Unit,
            @Assisted("onSearchClicked") onSearchClicked: () -> Unit,

            ): DefaultFavoriteComponent
    }


}