package com.example.weatherforecastapp.domane.usecase

import com.example.weatherforecastapp.domane.entity.City
import com.example.weatherforecastapp.domane.repository.FavoriteRepository
import javax.inject.Inject

class ChangeFavoriteStateUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {

    suspend fun addFavorite(city : City) = repository.addFavorite(city)

    suspend fun removeFavorite(cityId: Int) = repository.removeFavorite(cityId)

}