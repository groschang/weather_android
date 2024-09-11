package com.example.weather.repository

import com.example.weather.local.cities.entity.DBCity
import com.example.weather.model.City
import com.example.weather.model.WeatherForecast
import kotlinx.coroutines.flow.Flow

class WeatherRepositoryMock : WeatherRepository {

    lateinit var stubSearchCityResponse: List<City>
    var searchCityCallCount = 0
    var didSearchCity: () -> Unit = {}

    override suspend fun searchCity(id: String): List<City> {
        searchCityCallCount += 1
        didSearchCity()
        return stubSearchCityResponse
    }

    lateinit var stubFetchForecastResponse: WeatherForecast
    var fetchForecastCallCount = 0

    override suspend fun fetchForecast(locationKey: String): WeatherForecast {
        fetchForecastCallCount += 1
        return stubFetchForecastResponse
    }

    var storeCityCallCount = 0

    override suspend fun storeCity(city: City) {
        storeCityCallCount += 1
    }

    lateinit var stubGetCityResponse: Flow<DBCity?>
    var getCityCallCount = 0

    override suspend fun getCity(id: String): Flow<DBCity?> {
        getCityCallCount += 1
        return stubGetCityResponse
    }

    lateinit var stubGetCitiesResponse: Flow<List<DBCity>>
    var getCitiesCallCount = 0

    override suspend fun getCities(): Flow<List<DBCity>> {
        getCitiesCallCount += 1
        return stubGetCitiesResponse
    }

    var deleteCityCallCount = 0

    override suspend fun deleteCity(id: String) {
        deleteCityCallCount += 1
    }
}