package com.example.weather.repository

import com.example.weather.BuildConfig
import com.example.weather.local.cities.repository.CitiesRepository
import com.example.weather.local.cities.entity.DBCity
import com.example.weather.local.cities.entity.toDBCity
import com.example.weather.model.City
import com.example.weather.model.WeatherForecast
import com.example.weather.network.repository.WeatherDataSource
import kotlinx.coroutines.flow.Flow


class NetworkWeatherRepository(
    private val weatherDataSource: WeatherDataSource,
    private val weatherDatabase: CitiesRepository
) : WeatherRepository {

    override suspend fun searchCity(id: String): List<City> =
        weatherDataSource.searchCity(id, BuildConfig.API_KEY)

    override suspend fun fetchForecast(locationKey: String): WeatherForecast =
        weatherDataSource.forecast(locationKey, BuildConfig.API_KEY)

    override suspend fun storeCity(city: City) =
        weatherDatabase.insertCity(city.toDBCity())

    override suspend fun getCity(id: String): Flow<DBCity?> =
        weatherDatabase.getCityStream(id)

    override suspend fun getCities(): Flow<List<DBCity>> =
        weatherDatabase.getAllCitiesStream()

    override suspend fun deleteCity(id: String) =
        weatherDatabase.deleteCity(id)
}