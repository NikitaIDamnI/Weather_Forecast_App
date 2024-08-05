package com.example.weatherforecastapp.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherforecastapp.data.local.model.CityDbModel

@Database(
    entities = [CityDbModel::class],
    version = 1,
    exportSchema = false
)
abstract class FavoriteCityDatabase : RoomDatabase() {

    abstract fun FavoriteCitiesDao(): FavoriteCitiesDao

    companion object {
        private const val DB_NAME = "favorite_cities.db"
        private var instance: FavoriteCityDatabase? = null
        private val LOCK = Any()

        fun getInstance(context: Context): FavoriteCityDatabase {
            instance?.let { return it }

            synchronized(LOCK) {
                instance?.let { return it }
                val db = Room.databaseBuilder(
                    context = context,
                    klass = FavoriteCityDatabase::class.java,
                    name = DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                instance = db
                return db
            }

        }
    }
}