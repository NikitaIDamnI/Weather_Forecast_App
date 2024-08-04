package com.example.weatherforecastapp.domane.entity

data class Forecast (
    val currentWeather: Weather,
    val upcoming: List<Weather>
)
