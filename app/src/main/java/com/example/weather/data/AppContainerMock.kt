package com.example.weather.data

import android.content.Context
import com.example.weather.local.cities.database.CitiesDatabase
import com.example.weather.local.cities.repository.CitiesRepository
import com.example.weather.local.cities.repository.RoomCitiesRepository
import com.example.weather.local.settings.DataStoreSettingsRepository
import com.example.weather.local.settings.SettingsRepository
import com.example.weather.local.settings.dataStore
import com.example.weather.network.retrofit.MockRetrofitProvider
import com.example.weather.repository.NetworkWeatherRepository
import com.example.weather.repository.WeatherRepository
import com.example.weather.network.repository.WeatherDataSource
import com.example.weather.network.repository.WeatherWeatherDataSourceMock
import retrofit2.Retrofit


class AppContainerMock(
    private val context: Context
) : AppContainer {

    private val retrofit: Retrofit by lazy {
        MockRetrofitProvider.instance
    }

    private val dataSource: WeatherDataSource by lazy {
        retrofit.create(WeatherWeatherDataSourceMock::class.java)
    }

    private val database: CitiesDatabase by lazy {
        CitiesDatabase.getDatabase(context)
    }

    private val localRepository: CitiesRepository by lazy {
        RoomCitiesRepository(database.cityDao())
    }

    override val weatherRepository: WeatherRepository by lazy {
        NetworkWeatherRepository(
            weatherDataSource = dataSource,
            weatherDatabase = localRepository
        )
    }

    override val settingsRepository: SettingsRepository by lazy {
        DataStoreSettingsRepository(context.dataStore)
    }
}