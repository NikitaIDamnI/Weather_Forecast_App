package com.example.weatherforecastapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.defaultComponentContext
import com.example.weatherforecastapp.domane.usecase.ChangeFavoriteStateUseCase
import com.example.weatherforecastapp.domane.usecase.GetForecastWeatherUseCase
import com.example.weatherforecastapp.domane.usecase.SearchCityUseCase
import com.example.weatherforecastapp.presentation.root.DefaultRootComponent
import com.example.weatherforecastapp.presentation.root.RootContent
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var rootComponentFactory: DefaultRootComponent.Factory

    @Inject
    lateinit var searchCityUseCase: SearchCityUseCase

    @Inject
    lateinit var changeFavoriteStateUseCase: ChangeFavoriteStateUseCase

    @Inject
    lateinit var getForecastWeatherUseCase: GetForecastWeatherUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as WeatherApp).application.inject(this)
        super.onCreate(savedInstanceState)

        val root = rootComponentFactory.create(defaultComponentContext())
        enableEdgeToEdge()
        setContent {

            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) { padding ->
                RootContent(component = root, padding)

            }


        }
    }
}



