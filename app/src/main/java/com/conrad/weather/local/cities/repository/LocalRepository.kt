package com.conrad.weather.local.cities.repository

import com.conrad.weather.model.City
import kotlinx.coroutines.flow.Flow

interface LocalRepository {

    suspend fun insertCity(city: City)

    suspend fun deleteCity(id: String)

    suspend fun updateCity(city: City)

    fun getCityStream(id: String): Flow<City?>

    fun getAllCitiesStream(): Flow<List<City>>

    fun deleteAllCities()
}