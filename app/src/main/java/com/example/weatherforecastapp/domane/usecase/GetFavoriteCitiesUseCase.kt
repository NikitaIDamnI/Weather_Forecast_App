package com.example.weatherforecastapp.domane.usecase

import com.example.weatherforecastapp.domane.repository.FavoriteRepository
import javax.inject.Inject

class GetFavoriteCitiesUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    operator fun invoke() = repository.favoriteCities

}