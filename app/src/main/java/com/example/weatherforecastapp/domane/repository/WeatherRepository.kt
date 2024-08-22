package com.example.weatherforecastapp.domane.repository

import com.example.weatherforecastapp.domane.entity.Forecast
import com.example.weatherforecastapp.domane.entity.Weather

interface WeatherRepository {

    suspend fun getWeather(cityId: Int): Weather

    suspend fun getForecast(cityId: Int): Forecast


}