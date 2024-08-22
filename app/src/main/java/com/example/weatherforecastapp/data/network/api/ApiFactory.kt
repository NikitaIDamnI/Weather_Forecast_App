package com.example.weatherforecastapp.data.network.api

import com.example.weatherforecastapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.Locale

object ApiFactory {

    private const val BASE_URL = "https://api.weatherapi.com/v1/"
    private const val KEY_PARAM = "key"
    private const val LANG_PARAM = "lang"


    private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val newUrl = originalRequest
                .url
                .newBuilder()
                .addQueryParameter(KEY_PARAM, BuildConfig.WEATHER_API_KEY)
                .addQueryParameter(LANG_PARAM, Locale.getDefault().language)
                .build()
            val newRequest = originalRequest.newBuilder()
                .url(newUrl)
                .build()
            chain.proceed(newRequest)

        }
        .addInterceptor(httpLoggingInterceptor)
        .build()





    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
    val apiService: ApiService = retrofit.create()

}