package com.example.weather.ui.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.model.City
import com.example.weather.repository.WeatherRepository
import com.example.weather.utils.Result
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


class SearchViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {

    companion object {
        const val MIN_QUERY_LENGTH = 3
    }

    var uiState: Result<List<City>> by mutableStateOf(Result.Idle)

    var searchQuery: String by mutableStateOf("")
        private set

    fun onSearchQueryChange(query: String) {
        searchQuery = query
    }

    fun onSearchTriggered(query: String) {
        if (query.length >= MIN_QUERY_LENGTH) {
            searchCity(query)
        }
    }

    fun searchCity(city: String) {
        viewModelScope.launch {
            uiState = Result.Loading

            uiState = try {
                val result = weatherRepository.searchCity(city)
                if (result.isNotEmpty()) {
                    Result.Success(result)
                } else {
                    Result.NoResults
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
        if (searchQuery.length >= MIN_QUERY_LENGTH) {
            searchCity(searchQuery)
        } else {
            uiState = Result.Idle
        }
    }
}