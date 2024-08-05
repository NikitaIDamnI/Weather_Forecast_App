package com.example.weatherforecastapp.domane.entity

import java.util.Calendar

data class Weather(
    val tempC:Float,
    val maxTempC:Float = 0f,
    val minTempC:Float = 0f,
    val conditionText:String,
    val conditionIconUrl:String,
    val date:Calendar,
)
