package com.example.weatherforecastapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class ForecastDto(
    @SerializedName("forecastday") val forecastDayList: List<DayDto>
    )
