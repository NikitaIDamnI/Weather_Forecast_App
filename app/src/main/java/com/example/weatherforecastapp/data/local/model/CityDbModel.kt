package com.example.weatherforecastapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_cities")
data class CityDbModel (
    @PrimaryKey
    val id: Int,
    val name: String,
    val country: String,
    val lastUpdatedEpoch: Long,
    val thisTemp: String,
    val tempMaxMin: String,
    val conditionText: String,
    val conditionIconUrl: String
)