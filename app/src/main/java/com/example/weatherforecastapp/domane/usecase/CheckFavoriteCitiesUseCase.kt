package com.example.weatherforecastapp.domane.usecase

import com.example.weatherforecastapp.domane.repository.FavoriteRepository
import javax.inject.Inject

class CheckFavoriteCitiesUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    suspend operator fun invoke(cityId: Int) = repository.checkFavorite(cityId)

}