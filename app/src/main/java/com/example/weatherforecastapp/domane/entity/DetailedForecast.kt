package com.example.weatherforecastapp.domane.entity

import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailedForecast (
    val name : String,
    val value : Float,
    val conditionValue : ConditionValue,
    val colorConditionInt: Int,
    val progressValue: Int
): Parcelable{
    val colorCondition: Color
        get() = Color(colorConditionInt)

    constructor(
         name : String,
         value : Float,
         conditionValue : ConditionValue,
         colorCondition: Color,
         progressValue: Int
    ) : this(name, value, conditionValue, colorCondition.toArgb(),progressValue)


}

enum class ConditionValue(val value: String){
    KM_H("km/h"),PERCENT("%"),DEGREE("°")

}


