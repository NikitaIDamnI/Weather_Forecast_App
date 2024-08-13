package com.example.weatherforecastapp.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecastapp.data.local.model.CityDbModel
import kotlinx.coroutines.flow.Flow


@Dao
interface FavoriteCitiesDao {

    @Query("SELECT * FROM favorite_cities")
    fun getAll(): Flow<List<CityDbModel>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_cities WHERE id = :cityId LIMIT 1)")
    fun observeIsFavorite(cityId: Int): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_cities WHERE id = :cityId LIMIT 1)")
    suspend fun checkFavorite(cityId: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorite(city: CityDbModel)

    @Query("DELETE FROM favorite_cities WHERE id = :cityId")
    suspend fun removeFromFavorite(cityId: Int)
}