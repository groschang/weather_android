package com.conrad.weather.network.repository

import com.conrad.weather.model.City
import com.conrad.weather.model.WeatherForecast
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface WeatherDataSource {

    /**
     * Returns information for an array of cities that match the search text.
     */
    suspend fun searchCity(
        city: String,
        language: String? = null
    ): List<City>

    /**
     * Returns an array of daily forecasts for the next 10 days for a specific location.
     */
    suspend fun forecast(
        locationKey: String,
        language: String? = null
    ): WeatherForecast
}