package com.example.weatherforecastapp.data.repository

import com.example.weatherforecastapp.data.local.db.FavoriteCitiesDao
import com.example.weatherforecastapp.data.mapper.toDbModel
import com.example.weatherforecastapp.data.mapper.toEntities
import com.example.weatherforecastapp.data.network.api.ApiService
import com.example.weatherforecastapp.domane.entity.City
import com.example.weatherforecastapp.domane.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteCitiesDao: FavoriteCitiesDao,
) : FavoriteRepository {

    override val favoriteCities: Flow<List<City>>
        get() = favoriteCitiesDao.getAll()
            .map { it.toEntities() }

    override fun observeIsFavorite(cityId: Int): Flow<Boolean> {
        return favoriteCitiesDao.observeIsFavorite(cityId)
    }

    override suspend fun addFavorite(city: City) {
        favoriteCitiesDao.addToFavorite(city.toDbModel())
    }

    override suspend fun removeFavorite(cityId: Int) {
        favoriteCitiesDao.removeFromFavorite(cityId)
    }
}