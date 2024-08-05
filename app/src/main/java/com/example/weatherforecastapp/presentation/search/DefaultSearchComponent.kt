package com.example.weatherforecastapp.presentation.search

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore

import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.weatherforecastapp.domane.entity.SearchCity
import com.example.weatherforecastapp.presentation.extensions.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefaultSearchComponent @Inject constructor(
    componentContext: ComponentContext,
    private val storeFactory: SearchStoreFactory,
    private val openReason: OpenReason,
    private val onBackClick: () -> Unit,
    private val onSavedToFavorite: () -> Unit,
    private val onForecastForCityRequested: (SearchCity) -> Unit
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


}