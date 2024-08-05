package com.example.weatherforecastapp.data.repository

import com.example.weatherforecastapp.data.mapper.toForecast
import com.example.weatherforecastapp.data.mapper.toWeather
import com.example.weatherforecastapp.data.network.api.ApiService
import com.example.weatherforecastapp.domane.entity.Forecast
import com.example.weatherforecastapp.domane.entity.Weather
import com.example.weatherforecastapp.domane.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): WeatherRepository {
    
    override suspend fun getWeather(cityId: Int): Weather {
        return apiService.loadCurrentWeather("$PREFIX_CITY_ID$cityId").toWeather()
    }

    override suspend fun getForecast(cityId: Int): Forecast {
        return apiService.loadForecast("$PREFIX_CITY_ID$cityId").toForecast()
    }


    companion object{
            const val PREFIX_CITY_ID = "id:"
    }
}