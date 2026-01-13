package com.conrad.weather.local.cities.repository

import com.conrad.weather.local.cities.dao.CityDao
import com.conrad.weather.local.cities.entity.toCity
import com.conrad.weather.local.cities.entity.toDBCity
import com.conrad.weather.model.City
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WeatherLocalRepository(private val cityDao: CityDao) : LocalRepository {

    override suspend fun insertCity(city: City) = cityDao.insert(city.toDBCity())

    override suspend fun deleteCity(id: String) = cityDao.delete(id)

    override suspend fun updateCity(city: City) = cityDao.update(city.toDBCity())

    override fun getAllCitiesStream(): Flow<List<City>> =
        cityDao.getAllCities().map { list -> list.map { it.toCity() } }

    override fun getCityStream(id: String): Flow<City?> =
        cityDao.getCity(id).map { it?.toCity() }

    override fun deleteAllCities() = cityDao.deleteAllCities()
}