package com.example.weather.local.settings

import kotlinx.coroutines.flow.Flow


interface SettingsRepository {

    val currentCityId: Flow<String?>

    suspend fun saveCurrentCityId(cityId: String?)
}
