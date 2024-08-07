package com.example.weatherforecastapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import com.example.weatherforecastapp.data.network.api.ApiFactory
import com.example.weatherforecastapp.presentation.root.DefaultRootComponent
import com.example.weatherforecastapp.presentation.root.RootContent
import com.example.weatherforecastapp.presentation.ui.theme.WeatherForecastAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var rootComponentFactory: DefaultRootComponent.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as WeatherApp).application.inject(this)
        super.onCreate(savedInstanceState)
        val root = rootComponentFactory.create(defaultComponentContext())
        enableEdgeToEdge()
        setContent {
            RootContent(component = root)
        }
    }
}



