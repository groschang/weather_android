package com.example.weather.repository

import com.example.weather.local.cities.entity.DBCity
import com.example.weather.model.City
import com.example.weather.model.WeatherForecast
import kotlinx.coroutines.flow.Flow


interface WeatherRepository {

    suspend fun searchCity(id: String): List<City>

    suspend fun fetchForecast(locationKey: String): WeatherForecast

    suspend fun storeCity(city: City)

    suspend fun getCity(id: String): Flow<DBCity?>

    suspend fun getCities(): Flow<List<DBCity>>

    suspend fun deleteCity(id: String)
}