package com.example.weatherforecastapp

import android.app.Application
import com.example.weatherforecastapp.di.ApplicationComponent
import com.example.weatherforecastapp.di.DaggerApplicationComponent

class WeatherApp : Application() {
    lateinit var application: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        application = DaggerApplicationComponent.factory().create(this)
    }

}