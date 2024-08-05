package com.example.weatherforecastapp.domane.repository

import com.example.weatherforecastapp.domane.entity.City
import com.example.weatherforecastapp.domane.entity.SearchCity
import javax.inject.Inject

interface SearchRepository {

    suspend fun searchCities(query: String): List<SearchCity>

}