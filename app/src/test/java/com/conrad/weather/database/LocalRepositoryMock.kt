package com.conrad.weather.database

import com.conrad.weather.local.cities.repository.LocalRepository
import com.conrad.weather.model.City

import kotlinx.coroutines.flow.Flow

class LocalRepositoryMock : LocalRepository {

    var insertCityCallCount = 0

    override suspend fun insertCity(city: City) {
        insertCityCallCount += 1
    }

    var deleteCityCallCount = 0

    override suspend fun deleteCity(id: String) {
        deleteCityCallCount += 1
    }

    var updateCityCallCount = 0

    override suspend fun updateCity(city: City) {
        updateCityCallCount += 1
    }

    lateinit var stubGetCityStreamResponse: Flow<City?>
    var getCityStreamCallCount = 0

    override fun getCityStream(id: String): Flow<City?> {
        getCityStreamCallCount += 1
        return stubGetCityStreamResponse
    }

    lateinit var stubGetAllCitiesStreamResponse: Flow<List<City>>
    var getAllCitiesStreamCallCount = 0

    override fun getAllCitiesStream(): Flow<List<City>> {
        getAllCitiesStreamCallCount += 1
        return stubGetAllCitiesStreamResponse
    }

    var deleteAllCitiesCallCount = 0

    override fun deleteAllCities() {
        deleteAllCitiesCallCount += 1
    }

}