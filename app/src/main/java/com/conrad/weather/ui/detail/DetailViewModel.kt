package com.conrad.weather.ui.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.conrad.weather.local.settings.SettingsRepository
import com.conrad.weather.model.City
import com.conrad.weather.model.WeatherForecast
import com.conrad.weather.repository.WeatherRepository
import com.conrad.weather.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    var uiState: Result<WeatherForecast> by mutableStateOf(Result.Loading)

    var city: City? by mutableStateOf(null)

    init {
        loadCity()
    }

    private fun loadCity() {
        viewModelScope.launch {
            uiState = Result.Loading

            settingsRepository.currentCityId
                .onEmpty { uiState = Result.Idle }
                .collect { cityId ->
                    if (cityId != null) {
                        loadCityFromDatabase(cityId)
                    } else {
                        uiState = Result.Idle
                    }
                }
        }
    }

    private suspend fun loadCityFromDatabase(cityId: String) {
        viewModelScope.launch {
            weatherRepository.getCity(cityId)
                .onEmpty { uiState = Result.NoResults }
                .collect { item ->
                    city = item
                    loadForecast(cityId)
                }
        }
    }

    private suspend fun loadForecast(cityId: String) {
        viewModelScope.launch {
            uiState = try {
                val forecast = weatherRepository.fetchForecast(cityId)
                if (forecast != null) {
                    Result.Success(forecast)
                } else {
                    Result.Error(Exception("Forecast not found"))
                }
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