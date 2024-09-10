package com.example.weather.data

import android.content.Context
import com.example.weather.local.cities.database.CitiesDatabase
import com.example.weather.local.cities.repository.CitiesRepository
import com.example.weather.local.cities.repository.RoomCitiesRepository
import com.example.weather.local.settings.DataStoreSettingsRepository
import com.example.weather.local.settings.SettingsRepository
import com.example.weather.local.settings.dataStore
import com.example.weather.network.retrofit.RetrofitProvider
import com.example.weather.repository.NetworkWeatherRepository
import com.example.weather.repository.WeatherRepository
import com.example.weather.network.repository.NetworkWeatherDataSource
import com.example.weather.network.repository.WeatherDataSource


class DefaultAppContainer(
    private val context: Context
) : AppContainer {

    private val retrofit by lazy {
        RetrofitProvider.instance
    }

    private val dataSource: WeatherDataSource by lazy {
        retrofit.create(NetworkWeatherDataSource::class.java)
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

