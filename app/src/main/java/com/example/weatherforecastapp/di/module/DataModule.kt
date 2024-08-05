package com.example.weatherforecastapp.di.module

import android.content.Context
import com.example.weatherforecastapp.data.local.db.FavoriteCitiesDao
import com.example.weatherforecastapp.data.local.db.FavoriteCityDatabase
import com.example.weatherforecastapp.data.network.api.ApiFactory
import com.example.weatherforecastapp.data.network.api.ApiService
import com.example.weatherforecastapp.data.repository.FavoriteRepositoryImpl
import com.example.weatherforecastapp.data.repository.SearchRepositoryImpl
import com.example.weatherforecastapp.data.repository.WeatherRepositoryImpl
import com.example.weatherforecastapp.di.ApplicationScope
import com.example.weatherforecastapp.domane.repository.FavoriteRepository
import com.example.weatherforecastapp.domane.repository.SearchRepository
import com.example.weatherforecastapp.domane.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @[ApplicationScope Binds]
    fun bindWeatherRepository(impl: WeatherRepositoryImpl): WeatherRepository

    @[ApplicationScope Binds]
    fun bindFavoriteRepository(impl: FavoriteRepositoryImpl): FavoriteRepository

    @[ApplicationScope Binds]
    fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository


    companion object {

        @[ApplicationScope Provides]
        fun provideFavoriteDataBase(context: Context): FavoriteCityDatabase {
            return FavoriteCityDatabase.getInstance(context)
        }

        @[ApplicationScope Provides]
        fun provideApiFactory(): ApiService {
            return ApiFactory.apiService
        }

        @[ApplicationScope Provides]
        fun provideFavoriteCitiesDao(favoriteCityDatabase: FavoriteCityDatabase): FavoriteCitiesDao {
            return favoriteCityDatabase.FavoriteCitiesDao()
        }


    }


}