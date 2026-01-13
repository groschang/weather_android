package com.conrad.weather.repository

import com.conrad.weather.model.City
import com.conrad.weather.model.WeatherForecast

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

    var stubFetchForecastResponse: WeatherForecast? = null
    var fetchForecastCallCount = 0
    var didForecast: () -> Unit = {}

    override suspend fun fetchForecast(locationKey: String): WeatherForecast? {
        fetchForecastCallCount += 1
        didForecast()
        return stubFetchForecastResponse
    }

    var storeCityCallCount = 0

    override suspend fun storeCity(city: City) {
        storeCityCallCount += 1
    }

    lateinit var stubGetCityResponse: Flow<City?>
    var getCityCallCount = 0

    override suspend fun getCity(id: String): Flow<City?> {
        getCityCallCount += 1
        return stubGetCityResponse
    }

    lateinit var stubGetCitiesResponse: Flow<List<City>>
    var getCitiesCallCount = 0

    override suspend fun getCities(): Flow<List<City>> {
        getCitiesCallCount += 1
        return stubGetCitiesResponse
    }

    var deleteCityCallCount = 0

    override suspend fun deleteCity(id: String) {
        deleteCityCallCount += 1
    }
}