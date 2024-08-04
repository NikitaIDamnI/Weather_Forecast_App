package com.example.weatherforecastapp.domane.usecase

import com.example.weatherforecastapp.domane.repository.FavoriteRepository
import com.example.weatherforecastapp.domane.repository.SearchRepository
import javax.inject.Inject

class SearchCityUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(city: String) = repository.searchCities(city)

}