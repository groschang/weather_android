package com.example.weather.database

import com.example.weather.local.cities.entity.DBCity
import com.example.weather.local.cities.repository.CitiesRepository
import kotlinx.coroutines.flow.Flow

class CitiesRepositoryMock : CitiesRepository {

    var insertCityCallCount = 0

    override suspend fun insertCity(city: DBCity) {
        insertCityCallCount += 1
    }

    var deleteCityCallCount = 0

    override suspend fun deleteCity(id: String) {
        deleteCityCallCount += 1
    }

    var updateCityCallCount = 0

    override suspend fun updateCity(city: DBCity) {
        updateCityCallCount += 1
    }

    lateinit var stubGetCityStreamResponse: Flow<DBCity?>
    var getCityStreamCallCount = 0

    override fun getCityStream(id: String): Flow<DBCity?> {
        getCityStreamCallCount += 1
        return stubGetCityStreamResponse
    }

    lateinit var stubGetAllCitiesStreamResponse: Flow<List<DBCity>>
    var getAllCitiesStreamCallCount = 0

    override fun getAllCitiesStream(): Flow<List<DBCity>> {
        getAllCitiesStreamCallCount += 1
        return stubGetAllCitiesStreamResponse
    }

    var deleteAllCitiesCallCount = 0

    override fun deleteAllCities() {
        deleteAllCitiesCallCount += 1
    }

}