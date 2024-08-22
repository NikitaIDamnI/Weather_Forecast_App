package com.example.weatherforecastapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class WeatherDto(
    @SerializedName("last_updated_epoch") val lastUpdatedEpoch: Long,
    @SerializedName("temp_c") val tempC: Float,
    @SerializedName("condition") val condition: ConditionDto,
    @SerializedName("wind_kph") val windKmh: Float,
    @SerializedName("humidity") val humidity: Float,
    @SerializedName("precip_mm") val precipMM: Float,
    @SerializedName("feelslike_c") val feelslikeC: Float

)