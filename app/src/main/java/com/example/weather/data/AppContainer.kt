package com.example.weather.data

import com.example.weather.local.settings.SettingsRepository
import com.example.weather.repository.WeatherRepository


interface AppContainer {
    val weatherRepository: WeatherRepository
    val settingsRepository: SettingsRepository
}