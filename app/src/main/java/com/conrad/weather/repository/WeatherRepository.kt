package com.conrad.weather.repository

import com.conrad.weather.model.City
import com.conrad.weather.model.WeatherForecast
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun searchCity(query: String): List<City>

    suspend fun fetchForecast(locationKey: String): WeatherForecast?

    suspend fun storeCity(city: City)

    suspend fun updateCity(city: City)

    suspend fun getCity(id: String): Flow<City?>

    suspend fun getCities(): Flow<List<City>>

    suspend fun deleteCity(id: String)
}