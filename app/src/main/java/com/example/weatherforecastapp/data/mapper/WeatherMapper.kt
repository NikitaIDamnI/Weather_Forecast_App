package com.example.weatherforecastapp.data.mapper

import androidx.compose.ui.graphics.Color
import com.example.weatherforecastapp.data.network.dto.DayWeatherDto
import com.example.weatherforecastapp.data.network.dto.WeatherCurrentDto
import com.example.weatherforecastapp.data.network.dto.WeatherDto
import com.example.weatherforecastapp.data.network.dto.WeatherForecastDto
import com.example.weatherforecastapp.domane.entity.City
import com.example.weatherforecastapp.domane.entity.ConditionValue
import com.example.weatherforecastapp.domane.entity.DetailedForecast
import com.example.weatherforecastapp.domane.entity.Forecast
import com.example.weatherforecastapp.domane.entity.SearchCity
import com.example.weatherforecastapp.domane.entity.Weather
import com.example.weatherforecastapp.presentation.extensions.tempToFormattedString
import com.example.weatherforecastapp.presentation.ui.theme.ProgressBarColdColor
import com.example.weatherforecastapp.presentation.ui.theme.ProgressBarHighColor
import com.example.weatherforecastapp.presentation.ui.theme.ProgressBarLowColor
import com.example.weatherforecastapp.presentation.ui.theme.ProgressBarMiddleColor
import com.example.weatherforecastapp.presentation.ui.theme.ProgressBarNullColor
import java.util.Calendar
import java.util.Date

fun WeatherCurrentDto.toWeather(): Weather = current.toWeather()

fun WeatherDto.toWeather(): Weather = Weather(
    tempC = tempC.tempToFormattedString(),
    conditionText = condition.conditionText,
    conditionIconUrl = condition.conditionIconUrl.correctImageUrl(),
    date = lastUpdatedEpoch.toCalendarDto() ?: Calendar.getInstance(),
    detailedForecast = this.toDetailedForecast(),

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
            date = dayDto.date.toCalendarDto() ?: Calendar.getInstance(),
            detailedForecast = dayDto.dayWeatherDto.toDetailedForecast(),
        )
    }
)

private fun DayWeatherDto.toDetailedForecast(): List<DetailedForecast> {
    return builderListDetailedForecast(
        windValue = 0f, precipitationValue =0f, humidityValue =0f, feelsLike = 0f
    )
}
 fun WeatherDto.toDetailedForecast(): List<DetailedForecast> {
    return builderListDetailedForecast(
        windValue = windKmh,
        precipitationValue = precipMM,
        humidityValue = humidity,
        feelsLike = feelslikeC
    )
}


private fun builderListDetailedForecast(
    windValue: Float,
    precipitationValue: Float,
    humidityValue: Float,
    feelsLike: Float
): List<DetailedForecast> {
    return listOf(
        DetailedForecast(
            name = "Feels like",
            value = feelsLike,
            conditionValue = ConditionValue.DEGREE,
            progressValue = 1f,
            colorCondition = getColorByValue(windValue, ConditionValue.DEGREE.value)
        ),
        DetailedForecast(
            name = "Wind",
            value = windValue,
            conditionValue = ConditionValue.KM_H,
            progressValue = 1f,

            colorCondition = getColorByValue(windValue, ConditionValue.KM_H.value)
        ),
        DetailedForecast(
            name = "Precipitation",
            value = precipitationValue,
            conditionValue = ConditionValue.PERCENT,
            progressValue = precipitationValue / 100,

            colorCondition = getColorByValue(precipitationValue, ConditionValue.PERCENT.value)
        ),
        DetailedForecast(
            name = "Humidity",
            value = humidityValue,
            conditionValue = ConditionValue.PERCENT,
            progressValue = humidityValue / 100,
            colorCondition = getColorByValue(humidityValue, ConditionValue.PERCENT.value)
        ),

        )
}



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

private fun getColorByValue(value: Float, conditionValue: String): Color {
    return when (conditionValue) {
        "km/h" -> {
            when {
                value < 15 -> ProgressBarLowColor
                value >= 15 && value < 25 -> ProgressBarMiddleColor
                value >= 25 -> ProgressBarHighColor
                else -> ProgressBarNullColor
            }
        }

        "%" -> {
            when {
                value < 50 -> ProgressBarLowColor
                value >= 50 && value < 75 -> ProgressBarMiddleColor
                value >= 75 -> ProgressBarHighColor
                else -> ProgressBarNullColor
            }
        }

        "°" -> {
            return when {
                value < 0 -> ProgressBarColdColor
                value >= 0 && value < 10 -> ProgressBarLowColor
                value >= 10 && value < 25 -> ProgressBarMiddleColor
                value >= 25 -> ProgressBarHighColor
                else -> ProgressBarNullColor
            }
        }

        else -> throw RuntimeException("Unknown condition value: $conditionValue")
    }
}

