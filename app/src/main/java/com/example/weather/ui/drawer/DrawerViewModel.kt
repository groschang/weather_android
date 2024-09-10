package com.example.weather.ui.drawer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.local.cities.entity.DBCity
import com.example.weather.local.settings.SettingsRepository
import com.example.weather.repository.WeatherRepository
import kotlinx.coroutines.launch


class DrawerViewModel(
    private val weatherRepository: WeatherRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    var selectedCityId: String? by mutableStateOf(null)

    var cities: List<DBCity> by mutableStateOf(emptyList())

    init {
        loadCurrentCityId()
        loadLocations()
    }

    private fun loadCurrentCityId() {
        viewModelScope.launch {
            settingsRepository.currentCityId
                .collect { cityId ->
                    selectedCityId = cityId
                }
        }
    }

    private fun loadLocations() {
        viewModelScope.launch {
            weatherRepository.getCities()
                .collect { items ->
                    cities = items
                }
        }
    }

    fun selectCity(city: DBCity) {
        viewModelScope.launch {
            settingsRepository.saveCurrentCityId(city.id)
        }
    }
}

