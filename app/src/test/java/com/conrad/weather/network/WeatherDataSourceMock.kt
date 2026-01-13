package com.conrad.weather.network

import com.conrad.weather.model.City
import com.conrad.weather.model.WeatherForecast
import com.conrad.weather.network.repository.WeatherDataSource

class WeatherDataSourceMock : WeatherDataSource {

    lateinit var stubSearchCityResponse: List<City>
    var searchCityCallCount = 0
    var didSearchCity: () -> Unit = {}

    override suspend fun searchCity(city: String, language: String?): List<City> {
        searchCityCallCount += 1
        didSearchCity()
        return stubSearchCityResponse
    }

    lateinit var stubForecastResponse: WeatherForecast
    var forecastCallCount = 0
    var didForecast: () -> Unit = {}

    override suspend fun forecast(locationKey: String, language: String?): WeatherForecast {
        forecastCallCount += 1
        didForecast()
        return stubForecastResponse
    }
}