package com.example.weatherforecastapp.data.repository

import android.util.Log
import com.example.weatherforecastapp.data.local.db.FavoriteCitiesDao
import com.example.weatherforecastapp.data.mapper.toCalendar
import com.example.weatherforecastapp.data.mapper.toDbModel
import com.example.weatherforecastapp.data.mapper.toEntities
import com.example.weatherforecastapp.domane.entity.City
import com.example.weatherforecastapp.domane.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
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

    override suspend fun checkFromUpdate(city: City): Boolean {
        val checkFavorite = favoriteCitiesDao.checkFavorite(city.id)
        if (!checkFavorite) {
            return true
        } else {
            return checkTime(city)
        }
    }

    private fun checkTime(city: City): Boolean {
        val lastTimeUpdate = city.weather.date .apply {
            add(Calendar.MINUTE, 30)
        }
        Log.d("Repositor_Log", "lastTimeUpdate: ${lastTimeUpdate.time}")

        val thisTime = System.currentTimeMillis().toCalendar() ?: Calendar.getInstance()
        Log.d("Repositor_Log", "thisTime: ${thisTime.time}")

        val result = lastTimeUpdate < thisTime
        Log.d("Repositor_Log", "checkFavorite: $result")
        return result
    }
}