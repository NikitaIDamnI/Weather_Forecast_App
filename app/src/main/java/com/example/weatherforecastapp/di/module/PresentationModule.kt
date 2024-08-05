package com.example.weatherforecastapp.di.module

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import dagger.Module
import dagger.Provides

@Module
interface PresentationModule {

    companion object {

        @Provides
        fun provideStoreFactory(): StoreFactory = DefaultStoreFactory()


    }
}