package com.example.weather.local.cities.repository

import com.example.weather.local.cities.entity.DBCity
import kotlinx.coroutines.flow.Flow


interface CitiesRepository {

    suspend fun insertCity(city: DBCity)

    suspend fun deleteCity(id: String)

    suspend fun updateCity(city: DBCity)

    fun getCityStream(id: String): Flow<DBCity?>

    fun getAllCitiesStream(): Flow<List<DBCity>>

    fun deleteAllCities()
}
