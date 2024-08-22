package com.example.weatherforecastapp.domane.entity

import com.arkivanov.essenty.parcelable.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchCity(
    val id: Int,
    val name: String,
    val country: String,
): Parcelable
