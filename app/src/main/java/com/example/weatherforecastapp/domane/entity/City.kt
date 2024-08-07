package com.example.weatherforecastapp.domane.entity

import com.arkivanov.essenty.parcelable.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(
    val id: Int,
    val name: String,
    val country: String,
    val lastUpdatedEpoch: Long = 0,
    val thisTemp: String = "",
    val tempMaxMin: String = "",
    val conditionText: String = "",
    val conditionIconUrl: String = ""
) : Parcelable
