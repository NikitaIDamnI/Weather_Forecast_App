package com.example.weatherforecastapp.presentation.preview

import com.example.weatherforecastapp.domane.entity.City
import kotlinx.coroutines.flow.StateFlow

interface PreviewComponent {

    val model : StateFlow<PreviewStore.State>

    fun onClickAddToFavorite(city: City)

    fun onClickBack()

}