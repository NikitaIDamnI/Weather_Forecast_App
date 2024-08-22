package com.example.weatherforecastapp.domane.repository

import com.example.weatherforecastapp.domane.entity.SearchCity

interface SearchRepository {

    suspend fun searchCities(query: String): List<SearchCity>

}