package com.example.weatherforecastapp.domane.usecase

import com.example.weatherforecastapp.domane.entity.City
import com.example.weatherforecastapp.domane.repository.FavoriteRepository
import javax.inject.Inject

class CheckFromUpdateUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    suspend operator fun invoke(city: City): Boolean {
        return repository.checkFromUpdate(city)
    }
}