package com.example.weather.ui.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.local.cities.entity.DBCity
import com.example.weather.local.settings.SettingsRepository
import com.example.weather.model.WeatherForecast
import com.example.weather.repository.WeatherRepository
import com.example.weather.utils.Result
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


class DetailViewModel(
    private val weatherRepository: WeatherRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    var uiState: Result<WeatherForecast> by mutableStateOf(Result.Loading)

    var city: DBCity? by mutableStateOf(null)

    init {
        loadCity()
    }

    private fun loadCity() {
        viewModelScope.launch {
            settingsRepository.currentCityId
                .onEmpty { uiState = Result.Idle }
                .collect { cityId ->
                    if (cityId != null) {
                        loadCityFromDatabase(cityId)
                        loadForecast(cityId)
                    } else {
                        uiState = Result.Idle
                    }
                }
        }
    }

    private fun loadCityFromDatabase(cityId: String) {
        viewModelScope.launch {
            weatherRepository.getCity(cityId)
                .onEmpty { uiState = Result.NoResults }
                .collect { item ->
                    city = item
                }
        }
    }

    private fun loadForecast(cityId: String) {
        viewModelScope.launch {
            uiState = Result.Loading

            uiState = try {
                Result.Success(weatherRepository.fetchForecast(cityId))
            } catch (e: Exception) {
                when (e) {
                    is IOException -> Result.Error(e)
                    is HttpException -> Result.Error(e)
                    else -> Result.Error(e)
                }
            }
        }
    }

    fun retry() {
        if (city != null) {
            viewModelScope.launch {
                loadForecast(city!!.id)
            }
        } else {
            uiState = Result.Idle
        }
    }
}