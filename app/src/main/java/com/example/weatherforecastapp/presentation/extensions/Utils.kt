package com.example.weatherforecastapp.presentation.extensions

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt

fun ComponentContext.componentScope() = CoroutineScope(
    Dispatchers.Main.immediate + SupervisorJob()
).apply {
    lifecycle.doOnDestroy {
        cancel()
    }
}

fun Float.tempToFormattedString(): String = "${roundToInt()}°"



fun Calendar.formatDate(
    format: String = "EEEE, d MMM yyyy",
    locale: Locale = Locale.getDefault()
): String {
    val dateFormat = SimpleDateFormat(format, locale)
    return dateFormat.format(this.time).uppercase()
}