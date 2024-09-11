package com.example.weather.network

import com.example.weather.model.City
import com.example.weather.model.WeatherForecast
import com.example.weather.network.repository.WeatherDataSource

class WeatherDataSourceMock : WeatherDataSource {

    lateinit var stubSearchCityResponse: List<City>
    var searchCityCallCount = 0

    override suspend fun searchCity(city: String, apiKey: String, language: String?): List<City> {
        searchCityCallCount += 1
        return stubSearchCityResponse
    }

    lateinit var stubForecastResponse: WeatherForecast
    var forecastCallCount = 0

    override suspend fun forecast(locationKey: String, apiKey: String, language: String?): WeatherForecast {
        forecastCallCount += 1
        return stubForecastResponse
    }
}