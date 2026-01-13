package com.conrad.weather.ui.manage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.conrad.weather.local.settings.SettingsRepository
import com.conrad.weather.model.City
import com.conrad.weather.repository.WeatherRepository
import com.conrad.weather.utils.Result
import com.conrad.weather.utils.findAdjacent
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val settingsRepository: SettingsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        const val MAX_ITEMS = 14
    }

    var uiState: Result<List<City>> by mutableStateOf(Result.Loading)

    private val cityJson: String? = savedStateHandle[ManageDestination.itemArg]

    private var cities: List<City> by mutableStateOf(emptyList())

    val canAddCity: Boolean
        get() = cities.size < MAX_ITEMS

    var selectedCityId: String? by mutableStateOf(null)
        private set

    init {
        handleCity()
        loadCities()
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

    private fun handleCity() {
        if (cityJson != null) {
            try {
                viewModelScope.launch {
                    val city = Gson().fromJson(cityJson, City::class.java)
                    selectCity(city.id)
                    storeCity(city)
                }
            } catch (e: Exception) {
                uiState = Result.Error(e)
            }
        }
    }

    private fun loadCities() {
        viewModelScope.launch {
            weatherRepository.getCities().collect { result ->
                if (result.isNotEmpty()) {
                    cities = result
                    uiState = Result.Success(result)
                } else {
                    uiState = Result.NoResults
                    removeSelectedCity()
                }
            }
        }
    }

    fun selectCity(city: City) {
        viewModelScope.launch {
            selectCity(city.id)
            // Update the selectedCityId immediately for UI feedback
            selectedCityId = city.id
        }
    }

    private suspend fun storeCity(city: City) {
        // Simply store the city - the repository/database will handle conflicts
        weatherRepository.storeCity(city)
        
        // Reload cities after storing to ensure UI is updated
        loadCities()
    }

    private fun removeSelectedCity() {
        viewModelScope.launch {
            settingsRepository.saveCurrentCityId(null)
        }
    }

    private suspend fun selectCity(id: String) {
        settingsRepository.saveCurrentCityId(id)
    }

    fun removeCity(city: City) {
        viewModelScope.launch {
            if (city.id == selectedCityId) {
                checkSelectedCityId()
            }
            viewModelScope.launch {
                weatherRepository.deleteCity(city.id)
            }
            // Reload cities after deletion to ensure UI is updated
            loadCities()
        }
    }

    private suspend fun checkSelectedCityId() {
        selectedCityId?.let { cityId ->
            val index = cities.map { it.id }.findAdjacent(cityId)
            index?.let {
                settingsRepository.saveCurrentCityId(it)
            }
        }
    }
}