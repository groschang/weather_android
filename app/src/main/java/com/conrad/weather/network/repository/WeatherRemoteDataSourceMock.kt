package com.conrad.weather.network.repository

import com.conrad.weather.model.City
import com.conrad.weather.model.WeatherForecast
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherRemoteDataSourceMock : WeatherDataSource {

    @GET("cities.json")
    override suspend fun searchCity(
        @Query("q") city: String,
        @Query("language") language: String?
    ): List<City>

    @GET("forecast.json")
    override suspend fun forecast(
        @Query("locationKey") locationKey: String,
        @Query("language") language: String?
    ): WeatherForecast
}