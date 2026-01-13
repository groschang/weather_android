package com.conrad.weather.local.settings

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class DataStoreSettingsRepositoryMock : SettingsRepository {
    
    private val _currentCityId = MutableStateFlow<String?>("328328") // Default London
    
    override val currentCityId: Flow<String?>
        get() = _currentCityId

    override suspend fun saveCurrentCityId(cityId: String?) {
        _currentCityId.value = cityId
    }
}