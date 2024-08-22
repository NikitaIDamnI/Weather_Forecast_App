package com.example.weatherforecastapp.data.mapper

import com.example.weatherforecastapp.data.local.model.CityDbModel
import com.example.weatherforecastapp.domane.entity.City
import com.example.weatherforecastapp.domane.entity.Weather
import java.util.Date

import java.util.Calendar


fun City.toDbModel(): CityDbModel = with(this.weather) {
    CityDbModel(
        id,
        name,
        country,
        lastUpdatedEpoch = date.timeInMillis,
        thisTemp = tempC,
        tempMaxMin = maxTempC,
        conditionText = conditionText,
        conditionIconUrl = conditionIconUrl

    )
}

fun CityDbModel.toEntity(): City {
    val tempParts = tempMaxMin.split("-")
    val maxTemp = tempParts.getOrNull(0)?.trim() ?: "--°"
    val minTemp = tempParts.getOrNull(1)?.trim() ?: "--°"

    return City(
        id = id,
        name = name,
        country = country,
        weather = Weather(
            tempC = thisTemp,
            maxTempC = maxTemp,
            minTempC = minTemp,
            conditionText = conditionText,
            conditionIconUrl = conditionIconUrl,
            date = lastUpdatedEpoch.toCalendar() ?: Calendar.getInstance(),
        )
    )
}

fun List<CityDbModel>.toEntities(): List<City> = map { it.toEntity() }




