package com.example.weather.local.cities.repository

import com.example.weather.local.cities.dao.CityDao
import com.example.weather.local.cities.entity.DBCity
import kotlinx.coroutines.flow.Flow


class RoomCitiesRepository(private val cityDao: CityDao) : CitiesRepository {

    override suspend fun insertCity(city: DBCity) = cityDao.insert(city)

    override suspend fun deleteCity(id: String) = cityDao.delete(id)

    override suspend fun updateCity(city: DBCity) = cityDao.update(city)

    override fun getAllCitiesStream(): Flow<List<DBCity>> = cityDao.getAllCities()

    override fun getCityStream(id: String): Flow<DBCity?> = cityDao.getCity(id)

    override fun deleteAllCities() = cityDao.deleteAllCities()
}