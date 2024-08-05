package com.example.weatherforecastapp.presentation.details

import kotlinx.coroutines.flow.StateFlow

interface DetailsComponent {

    val model : StateFlow<DetailsStore.State>

    fun onClickBack()

    fun onClickFavoriteStatus()

}