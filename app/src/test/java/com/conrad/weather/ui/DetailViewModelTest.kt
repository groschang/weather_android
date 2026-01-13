package com.conrad.weather.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.conrad.weather.repository.WeatherRepositoryMock
import com.conrad.weather.local.settings.DataStoreSettingsRepositoryMock
import com.conrad.weather.ui.detail.DetailViewModel
import com.conrad.weather.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()

    private lateinit var weatherRepository: WeatherRepositoryMock
    private lateinit var settingsRepository: DataStoreSettingsRepositoryMock
    private lateinit var sut: DetailViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        weatherRepository = WeatherRepositoryMock()
        settingsRepository = DataStoreSettingsRepositoryMock()
        sut = DetailViewModel(weatherRepository, settingsRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState is Success with mock data initially`() {
        // The mock repository returns mock data by default
        // So we expect a Success state with the mock forecast
        assertEquals(true, sut.uiState is Result.Success)
    }

    @Test
    fun `retry loads forecast when city is not null`() = runTest {
        // Given
        // The mock repository returns mock data by default
        
        // When
        sut.retry()

        // Then
        // We expect the uiState to remain as Success
        assertEquals(true, sut.uiState is Result.Success)
    }

    @Test
    fun `retry sets uiState to Idle when city is null`() = runTest {
        // Given
        // Set the current city ID to null to simulate no city selected
        settingsRepository.saveCurrentCityId(null)
        
        // Create a new ViewModel instance to reflect the change
        sut = DetailViewModel(weatherRepository, settingsRepository)

        // When
        sut.retry()

        // Then
        // We expect the uiState to be Idle since there's no city to load
        assertEquals(Result.Idle, sut.uiState)
    }
}