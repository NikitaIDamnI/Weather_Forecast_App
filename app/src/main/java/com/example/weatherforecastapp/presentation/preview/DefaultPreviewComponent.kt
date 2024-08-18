package com.example.weatherforecastapp.presentation.preview

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.weatherforecastapp.domane.entity.City
import com.example.weatherforecastapp.domane.entity.Forecast
import com.example.weatherforecastapp.presentation.extensions.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultPreviewComponent @AssistedInject constructor(
    private val storeFactory: PreviewStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("city") city: City,
    @Assisted("forecast") forecast: Forecast,
    @Assisted("isFavorite") isFavorite: Boolean,
    @Assisted("onClickAddToFavorite") onClickAddToFavorite: (City) -> Unit,
    @Assisted("onBackFromPreview") onBackFromPreview: () -> Unit
) : PreviewComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        storeFactory.create(
            city = city,
            forecast = forecast,
            isFavorite = isFavorite,
        )
    }

    private val scope = componentContext.componentScope()

    init {
        scope.launch {
            store.labels.collect{
                when(it){
                    is PreviewStore.Label.AddToFavorite -> { onClickAddToFavorite(it.city) }
                    PreviewStore.Label.BackFromPreview -> onBackFromPreview()
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<PreviewStore.State> = store.stateFlow

    override fun onClickAddToFavorite(city: City) {
        store.accept(PreviewStore.Intent.AddToFavorite(city))
    }

    override fun onClickBack() {
        store.accept(PreviewStore.Intent.BackFromPreview)
    }
}