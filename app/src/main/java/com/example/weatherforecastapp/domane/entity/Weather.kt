package com.example.weatherforecastapp.domane.entity

import com.arkivanov.essenty.parcelable.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Calendar

@Parcelize
data class Weather(
    val tempC:String = "",
    val maxTempC:String = "",
    val minTempC:String = "",
    val conditionText:String  = "",
    val conditionIconUrl:String = "",
    val date:Calendar = Calendar.getInstance(),
):Parcelable
