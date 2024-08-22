package com.example.weatherforecastapp.domane.repository

import com.example.weatherforecastapp.domane.entity.City
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    val favoriteCities: Flow<List<City>>

    fun observeIsFavorite(cityId: Int): Flow<Boolean>

    suspend fun addFavorite(city: City)

    suspend fun removeFavorite(cityId: Int)

    suspend fun checkFromUpdate(city: City): Boolean

    suspend fun checkFavorite(cityId: Int): Boolean

}