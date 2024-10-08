package com.example.weatherforecastapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class ConditionDto(
    @SerializedName("text") val conditionText: String,
    @SerializedName("icon") val conditionIconUrl: String,
)
