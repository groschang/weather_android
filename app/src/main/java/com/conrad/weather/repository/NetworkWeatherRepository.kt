package com.conrad.weather.repository

import androidx.room.Query
import com.conrad.weather.local.cities.repository.LocalRepository
import com.conrad.weather.model.City
import com.conrad.weather.model.WeatherForecast
import com.conrad.weather.network.repository.WeatherDataSource
import kotlinx.coroutines.flow.Flow

class NetworkWeatherRepository(
    private val dataSource: WeatherDataSource,
    private val database: LocalRepository
) : WeatherRepository {

    override suspend fun searchCity(query: String): List<City> =
        dataSource.searchCity(query)

    override suspend fun fetchForecast(locationKey: String): WeatherForecast =
        dataSource.forecast(locationKey)

    override suspend fun storeCity(city: City) =
        database.insertCity(city)

    override suspend fun updateCity(city: City) =
        database.updateCity(city)

    override suspend fun getCity(id: String): Flow<City?> =
        database.getCityStream(id)

    override suspend fun getCities(): Flow<List<City>> =
        database.getAllCitiesStream()

    override suspend fun deleteCity(id: String) =
        database.deleteCity(id)
}