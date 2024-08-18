package com.example.weatherforecastapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class DayWeatherDto (
    @SerializedName("avgtemp_c") val tempC: Float,
    @SerializedName("maxtemp_c") val maxTempC: Float,
    @SerializedName("mintemp_c") val minTempC: Float,
    @SerializedName("maxwind_kph") val maxWindKph: Float,
    @SerializedName("condition") val conditionDto: ConditionDto,


    )