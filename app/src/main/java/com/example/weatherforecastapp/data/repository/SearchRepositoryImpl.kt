package com.example.weatherforecastapp.data.repository

import com.example.weatherforecastapp.data.mapper.toSearchCities
import com.example.weatherforecastapp.data.network.api.ApiService
import com.example.weatherforecastapp.domane.entity.City
import com.example.weatherforecastapp.domane.entity.SearchCity
import com.example.weatherforecastapp.domane.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl  @Inject constructor(
    private val apiService: ApiService
): SearchRepository {

    override suspend fun searchCities(query: String): List<SearchCity> {
        val response = apiService.searchCity(query)
        return response.toSearchCities()
    }
}