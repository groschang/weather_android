package com.example.weather.network.repository

import com.example.weather.model.City
import com.example.weather.model.WeatherForecast
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface WeatherDataSource {

    /**
     * Returns information for an array of cities that match the search text.
     */
    suspend fun searchCity(
        city: String,
        apiKey: String,
        language: String? = null
    ): List<City>

    /**
     * Returns an array of daily forecasts for the next 10 days for a specific location.
     * Forecast searches require a location key. Please use the Locations API to obtain the location
     */
    @GET("/forecasts/v1/daily/5day") //TODO: change me to 10
    suspend fun forecast(
        locationKey: String,
        apiKey: String,
        language: String? = null
    ): WeatherForecast
}

interface NetworkWeatherDataSource : WeatherDataSource {

    @GET("/locations/v1/cities/search")
    override suspend fun searchCity(
        @Query("q") city: String,
        @Query("apikey") apiKey: String,
        @Query("language") language: String?
    ): List<City>

    @GET("/forecasts/v1/daily/5day/{locationKey}") //TODO: change me to 10
    override suspend fun forecast(
        @Path("locationKey") locationKey: String,
        @Query("apikey") apiKey: String,
        @Query("language") language: String?
    ): WeatherForecast
}


interface WeatherWeatherDataSourceMock : WeatherDataSource {

    @GET("cities.json")
    override suspend fun searchCity(
        @Query("q") city: String,
        @Query("apikey") apiKey: String,
        @Query("language") language: String?
    ): List<City>

    @GET("forecast.json")
    override suspend fun forecast(
        @Query("locationKey") locationKey: String,
        @Query("apikey") apiKey: String,
        @Query("language") language: String?
    ): WeatherForecast

}



