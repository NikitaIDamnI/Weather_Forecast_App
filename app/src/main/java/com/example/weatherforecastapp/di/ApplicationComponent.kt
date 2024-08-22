package com.example.weatherforecastapp.di

import android.content.Context
import com.example.weatherforecastapp.MainActivity
import com.example.weatherforecastapp.di.module.DataModule
import com.example.weatherforecastapp.di.module.PresentationModule
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        PresentationModule::class,
    ]
)
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)


    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): ApplicationComponent

    }


}