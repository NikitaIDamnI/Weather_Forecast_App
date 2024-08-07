package com.example.weatherforecastapp.presentation.search

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore

import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.weatherforecastapp.domane.entity.SearchCity
import com.example.weatherforecastapp.presentation.extensions.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefaultSearchComponent @AssistedInject constructor(
    private val storeFactory: SearchStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("openReason") private val openReason: OpenReason,
    @Assisted("onBackClick") private val onBackClick: () -> Unit,
    @Assisted("onSavedToFavorite") private val onSavedToFavorite: () -> Unit,
    @Assisted("onForecastForCityRequested") private val onForecastForCityRequested: (SearchCity) -> Unit
) : SearchComponent, ComponentContext by componentContext {


    private val store = instanceKeeper.getStore { storeFactory.create(openReason) }
    private val scope = componentContext.componentScope()


    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<SearchStore.State> = store.stateFlow

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    SearchStore.Label.ClickBack -> onBackClick()
                    is SearchStore.Label.OpenForecast -> onForecastForCityRequested(it.city)
                    SearchStore.Label.SavedToFavorite -> onSavedToFavorite()
                }
            }
        }
    }


    override fun changeSearchQuery(query: String) {
        store.accept(SearchStore.Intent.SearchQueryChanged(query))
    }

    override fun onClickSearch() {
        store.accept(SearchStore.Intent.ClickSearch)
    }

    override fun onClickCity(searchCity: SearchCity) {
        store.accept(SearchStore.Intent.ClickCity(searchCity))
    }

    override fun onClickBack() {
        store.accept(SearchStore.Intent.ClickBack)
    }


    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("openReason") openReason: OpenReason,
            @Assisted("onBackClick") onBackClick: () -> Unit,
            @Assisted("onSavedToFavorite") onSavedToFavorite: () -> Unit,
            @Assisted("onForecastForCityRequested") onForecastForCityRequested: (SearchCity) -> Unit
        ): DefaultSearchComponent
    }

}