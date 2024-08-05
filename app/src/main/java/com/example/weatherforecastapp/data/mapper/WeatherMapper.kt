package com.example.weatherforecastapp.data.mapper

import com.example.weatherforecastapp.data.network.dto.WeatherCurrentDto
import com.example.weatherforecastapp.data.network.dto.WeatherDto
import com.example.weatherforecastapp.data.network.dto.WeatherForecastDto
import com.example.weatherforecastapp.domane.entity.Forecast
import com.example.weatherforecastapp.domane.entity.Weather
import java.util.Calendar
import java.util.Date

fun WeatherCurrentDto.toWeather(): Weather = current.toWeather()

fun WeatherDto.toWeather(): Weather = Weather(
    tempC = tempC,
    conditionText = condition.conditionText,
    conditionIconUrl = condition.conditionIconUrl.correctImageUrl(),
    date = lastUpdatedEpoch.toCalendar()
)

fun WeatherForecastDto.toForecast(): Forecast = Forecast(
    currentWeather = currentWeather.toWeather(),
    upcoming = forecast.forecastDayList.map { dayDto ->
        val dayWeatherDto = dayDto.dayWeatherDto
        Weather(
            tempC = dayWeatherDto.tempC,
            maxTempC = dayWeatherDto.maxTempC,
            minTempC = dayWeatherDto.minTempC,
            conditionText = dayWeatherDto.conditionDto.conditionText,
            conditionIconUrl = dayWeatherDto.conditionDto.conditionIconUrl.correctImageUrl(),
            date = dayDto.date.toCalendar()
        )
    }
)

private fun Long.toCalendar() = Calendar.getInstance().apply {
    time = Date(this@toCalendar * 1000)
}

private fun String.correctImageUrl(): String = "https:$this".replace("64x64", "128x128")


