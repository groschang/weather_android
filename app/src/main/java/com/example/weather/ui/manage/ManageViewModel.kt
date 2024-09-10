package com.example.weather.ui.manage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.local.cities.entity.DBCity
import com.example.weather.local.settings.SettingsRepository
import com.example.weather.model.City
import com.example.weather.repository.WeatherRepository
import com.example.weather.utils.Result
import com.example.weather.utils.findAdjacent
import com.google.gson.Gson
import kotlinx.coroutines.launch


class ManageViewModel(
    private val weatherRepository: WeatherRepository,
    private val settingsRepository: SettingsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        const val MAX_ITEMS = 5
    }

    var uiState: Result<List<DBCity>> by mutableStateOf(Result.Loading)

    private val cityJson: String? = savedStateHandle[ManageDestination.itemArg]

    private var cities: List<DBCity> by mutableStateOf(emptyList())

    val canAddCity: Boolean
        get() = cities.size < MAX_ITEMS

    var selectedCityId: String? by mutableStateOf(null)
        private set

    init {
        loadCurrentCityId()
        handleCity()
        loadCities()
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
                    viewModelScope.launch {
                        storeCity(city)
                    }
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

    fun selectCity(city: DBCity) {
        viewModelScope.launch {
            selectCity(city.id)
        }
    }

    private suspend fun storeCity(city: City) {
        weatherRepository.getCity(city.id)
            .collect { item ->
                if (item == null) {
                    weatherRepository.storeCity(city)
                }
            }
    }

    private fun removeSelectedCity() {
        viewModelScope.launch {
            settingsRepository.saveCurrentCityId(null)
        }
    }

    private suspend fun selectCity(id: String) {
        settingsRepository.saveCurrentCityId(id)
    }

    fun removeCity(city: DBCity) {
        viewModelScope.launch {
            if (city.id == selectedCityId) {
                checkSelectedCityId()
            }
            viewModelScope.launch {
                weatherRepository.deleteCity(city.id)
            }
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