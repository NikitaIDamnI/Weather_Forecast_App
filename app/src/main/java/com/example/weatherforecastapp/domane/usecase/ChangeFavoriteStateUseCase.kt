package com.example.weatherforecastapp.domane.usecase

import com.example.weatherforecastapp.domane.repository.FavoriteRepository
import javax.inject.Inject

class ChangeFavoriteStateUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {

    suspend fun addFavorite(cityId: Int) = repository.addFavorite(cityId)

    suspend fun removeFavorite(cityId: Int) = repository.removeFavorite(cityId)

}