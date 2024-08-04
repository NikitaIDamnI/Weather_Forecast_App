package com.example.weatherforecastapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class WeatherForecastDto(
    @SerializedName("current") val currentWeather: WeatherDto,
    @SerializedName("forecast") val forecast: ForecastDto
)
