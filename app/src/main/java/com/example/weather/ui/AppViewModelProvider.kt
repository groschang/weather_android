package com.example.weather.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.weather.WeatherApplication
import com.example.weather.ui.detail.DetailViewModel
import com.example.weather.ui.drawer.DrawerViewModel
import com.example.weather.ui.manage.ManageViewModel
import com.example.weather.ui.search.SearchViewModel

object AppViewModelProvider {

    val Factory = viewModelFactory {

        initializer {
            DetailViewModel(
                weatherRepository = inventoryApplication().container.weatherRepository,
                settingsRepository = inventoryApplication().container.settingsRepository
            )
        }

        initializer {
            SearchViewModel(
                weatherRepository = inventoryApplication().container.weatherRepository
            )
        }

        initializer {
            ManageViewModel(
                weatherRepository = inventoryApplication().container.weatherRepository,
                settingsRepository = inventoryApplication().container.settingsRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }

        initializer {
            DrawerViewModel(
                weatherRepository = inventoryApplication().container.weatherRepository,
                settingsRepository = inventoryApplication().container.settingsRepository
            )
        }

    }
}

fun CreationExtras.inventoryApplication(): WeatherApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as WeatherApplication)
