package com.example.weatherforecastapp.domane.usecase

import com.example.weatherforecastapp.domane.repository.FavoriteRepository
import com.example.weatherforecastapp.domane.repository.WeatherRepository
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(cityId: Int) = repository.getWeather(cityId)

}