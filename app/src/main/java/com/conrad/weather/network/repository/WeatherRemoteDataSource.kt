package com.conrad.weather.network.repository

import com.conrad.weather.model.City
import com.conrad.weather.model.WeatherForecast
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherRemoteDataSource : WeatherDataSource {

    /**
     * Returns information for an array of cities that match the search text.
     */
    @GET("/locations/v1/cities/search")
    override suspend fun searchCity(
        @Query("q") city: String,
        @Query("language") language: String?
    ): List<City>

    /**
     * Returns an array of daily forecasts for the next 10 days for a specific location.
     * Forecast searches require a location key. Please use the Locations API to obtain the location
     * https://developer.accuweather.com/core-weather/location-key-daily
     */
    @GET("/forecasts/v1/daily/5day/{locationKey}")
    override suspend fun forecast(
        @Path("locationKey") locationKey: String,
        @Query("language") language: String?
    ): WeatherForecast
}