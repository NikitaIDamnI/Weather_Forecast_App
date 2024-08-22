package com.example.weatherforecastapp.presentation.search

import com.example.weatherforecastapp.domane.entity.City
import com.example.weatherforecastapp.domane.entity.SearchCity
import kotlinx.coroutines.flow.StateFlow

interface SearchComponent {

    val model : StateFlow<SearchStore.State>

    fun changeSearchQuery (query: String)

    fun onClickSearch()

    fun onClickCity (searchCity: SearchCity)

    fun onClickBack ()

    fun onClickClosePreview()

    fun onClickAddFavorite(city: City)



}