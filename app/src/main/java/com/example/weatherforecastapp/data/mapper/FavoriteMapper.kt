package com.example.weatherforecastapp.data.mapper

import com.example.weatherforecastapp.data.local.model.CityDbModel
import com.example.weatherforecastapp.domane.entity.City


fun City.toDbModel(): CityDbModel = CityDbModel(
    id,
    name,
    country,
    lastUpdatedEpoch,
    thisTemp,
    tempMaxMin,
    conditionText,
    conditionIconUrl
)

fun CityDbModel.toEntity(): City = City(
    id,
    name,
    country,
    lastUpdatedEpoch,
    thisTemp,
    tempMaxMin,
    conditionText,
    conditionIconUrl
)

fun List<CityDbModel>.toEntities(): List<City> = map { it.toEntity() }


