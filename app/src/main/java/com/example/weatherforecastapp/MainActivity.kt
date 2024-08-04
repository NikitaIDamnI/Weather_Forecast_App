package com.example.weatherforecastapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.weatherforecastapp.data.network.api.ApiFactory
import com.example.weatherforecastapp.presentation.ui.theme.WeatherForecastAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiService = ApiFactory.apiService

        CoroutineScope(Dispatchers.IO).launch {
            val currentWeather = apiService.loadCurrentWeather("Moscow")
            val forecast = apiService.loadForecast("Moscow")
            val searchCity = apiService.searchCity("Moscow")
           Log.d(
               "MainActivity", "Current weather: ${currentWeather}" +
                       " Forecast: ${forecast}" +
                       " Search city: ${searchCity}"
           )
        }

        enableEdgeToEdge()
        setContent {
            WeatherForecastAppTheme {

            }
        }
    }
}



