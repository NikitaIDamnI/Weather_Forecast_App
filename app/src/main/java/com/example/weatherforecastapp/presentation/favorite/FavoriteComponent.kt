package com.example.weatherforecastapp.presentation.favorite

import com.example.weatherforecastapp.domane.entity.City
import kotlinx.coroutines.flow.StateFlow

interface FavoriteComponent {

    val model: StateFlow<FavoriteStore.State>

    fun onClickSearch()

    fun onCityItemClick(indexCity : Int,cities: List<City>)

    fun onDeleteCity(cityId: Int)



}