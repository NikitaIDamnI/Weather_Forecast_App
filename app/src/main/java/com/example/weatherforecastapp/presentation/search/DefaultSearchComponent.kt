package com.example.weatherforecastapp.presentation.search

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.weatherforecastapp.domane.entity.City
import com.example.weatherforecastapp.domane.entity.SearchCity
import com.example.weatherforecastapp.presentation.extensions.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultSearchComponent @AssistedInject constructor(
    private val storeFactory: SearchStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("onBackClick") private val onBackClick: () -> Unit,
    @Assisted("onClickAddFavorite") private val onClickAddFavorite: () -> Unit
) : SearchComponent, ComponentContext by componentContext {


    private val store = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentContext.componentScope()


    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<SearchStore.State> = store.stateFlow

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    SearchStore.Label.ClickBack -> onBackClick()
                    SearchStore.Label.ClickAddFavorite -> onClickAddFavorite()
                }
            }
        }
    }


    override fun changeSearchQuery(query: String) {
        store.accept(SearchStore.Intent.ChangeSearchQuery(query))
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

    override fun onClickClosePreview() {
        store.accept(SearchStore.Intent.ClickClosePreview)
    }

    override fun onClickAddFavorite(city: City) {
        store.accept(SearchStore.Intent.ClickAddFavorite(city))
    }


    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("onBackClick") onBackClick: () -> Unit,
            @Assisted("onClickAddFavorite") onClickAddFavorite: () -> Unit
        ): DefaultSearchComponent
    }

}