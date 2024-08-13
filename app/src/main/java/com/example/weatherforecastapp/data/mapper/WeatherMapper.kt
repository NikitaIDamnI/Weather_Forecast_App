package com.example.weatherforecastapp.data.mapper

import com.example.weatherforecastapp.data.network.dto.WeatherCurrentDto
import com.example.weatherforecastapp.data.network.dto.WeatherDto
import com.example.weatherforecastapp.data.network.dto.WeatherForecastDto
import com.example.weatherforecastapp.domane.entity.City
import com.example.weatherforecastapp.domane.entity.Forecast
import com.example.weatherforecastapp.domane.entity.SearchCity
import com.example.weatherforecastapp.domane.entity.Weather
import com.example.weatherforecastapp.presentation.extensions.tempToFormattedString
import java.util.Calendar
import java.util.Date

fun WeatherCurrentDto.toWeather(): Weather = current.toWeather()

fun WeatherDto.toWeather(): Weather = Weather(
    tempC = tempC.tempToFormattedString(),
    conditionText = condition.conditionText,
    conditionIconUrl = condition.conditionIconUrl.correctImageUrl(),
    date = lastUpdatedEpoch.toCalendarDto() ?: Calendar.getInstance()
)

fun WeatherForecastDto.toForecast(): Forecast = Forecast(
    currentWeather = currentWeather.toWeather(),
    upcoming = forecast.forecastDayList.map { dayDto ->
        val dayWeatherDto = dayDto.dayWeatherDto
        Weather(
            tempC = dayWeatherDto.tempC.tempToFormattedString(),
            maxTempC = dayWeatherDto.maxTempC.tempToFormattedString(),
            minTempC = dayWeatherDto.minTempC.tempToFormattedString(),
            conditionText = dayWeatherDto.conditionDto.conditionText,
            conditionIconUrl = dayWeatherDto.conditionDto.conditionIconUrl.correctImageUrl(),
            date = dayDto.date.toCalendarDto() ?: Calendar.getInstance()
        )
    }
)

fun Weather.formatedToCity(city: SearchCity) = City(
    id = city.id,
    name = city.name,
    country = city.country,
    weather = this
)

fun City.addWeather(weather: Weather) = City(
    id = this.id,
    name = this.name,
    country = this.country,
    weather = weather
)



 private fun Long.toCalendarDto(): Calendar? = Calendar.getInstance().apply {
    time = Date(this@toCalendarDto * 1000)
}
fun Long.toCalendar() = Calendar.getInstance().apply {
    time = Date(this@toCalendar)
}

private fun String.correctImageUrl(): String = "https:$this".replace("64x64", "128x128")


