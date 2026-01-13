package com.conrad.weather.ui.drawer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.conrad.weather.local.settings.DataStoreSettingsRepositoryMock
import com.conrad.weather.local.settings.SettingsRepository
import com.conrad.weather.model.City
import com.conrad.weather.repository.WeatherRepository
import com.conrad.weather.repository.WeatherRepositoryMock
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrawerViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    var selectedCityId: String? by mutableStateOf(null)

    var cities: List<City> by mutableStateOf(emptyList())

    init {
        loadLocations()
        loadCurrentCityId()
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

    fun selectCity(city: City) {
        viewModelScope.launch {
            settingsRepository.saveCurrentCityId(city.id)
            // Update selectedCityId immediately for UI feedback
            selectedCityId = city.id
        }
    }
}

object DrawerViewModelFactory {
    fun createMock(): DrawerViewModel {
        return DrawerViewModel(
            weatherRepository = WeatherRepositoryMock(),
            settingsRepository = DataStoreSettingsRepositoryMock()
        )
    }
}
