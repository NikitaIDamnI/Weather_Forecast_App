package com.example.weatherforecastapp.domane.usecase

import com.example.weatherforecastapp.domane.repository.WeatherRepository
import javax.inject.Inject

class GetForecastWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(cityId: Int) = repository.getForecast(cityId)
}