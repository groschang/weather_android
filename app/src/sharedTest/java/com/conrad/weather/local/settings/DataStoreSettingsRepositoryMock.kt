package com.conrad.weather.local.settings

import com.conrad.weather.local.settings.SettingsRepository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class DataStoreSettingsRepositoryMock(initialCityId: String? = null) : SettingsRepository {

    var stubCurrentCityIdResponse: MutableStateFlow<String?> = MutableStateFlow(initialCityId)

    override val currentCityId: Flow<String?> =
        stubCurrentCityIdResponse

    var saveCurrentCityIdCallCount = 0

    override suspend fun saveCurrentCityId(cityId: String?) {
        saveCurrentCityIdCallCount += 1
    }
}