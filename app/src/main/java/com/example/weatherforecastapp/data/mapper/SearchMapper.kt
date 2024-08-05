package com.example.weatherforecastapp.data.mapper

import com.example.weatherforecastapp.data.network.dto.CityDto
import com.example.weatherforecastapp.domane.entity.City
import com.example.weatherforecastapp.domane.entity.SearchCity

fun CityDto.toSearchCity(): SearchCity = SearchCity(id, name, country)

fun List<CityDto>.toSearchCities(): List<SearchCity> = map { it.toSearchCity() }