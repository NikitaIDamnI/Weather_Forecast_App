package com.example.weatherforecastapp.domane.repository

import com.example.weatherforecastapp.domane.entity.City

interface SearchRepository {

    suspend fun searchCities(query: String): List<City>

}