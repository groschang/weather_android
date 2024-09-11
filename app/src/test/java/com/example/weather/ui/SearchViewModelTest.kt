package com.example.weather.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weather.repository.WeatherRepositoryMock
import com.example.weather.model.stubs.CitiesStub
import com.example.weather.ui.search.SearchViewModel
import com.example.weather.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException


class SearchViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private lateinit var mockWeatherRepository: WeatherRepositoryMock
    private lateinit var sut: SearchViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        mockWeatherRepository = WeatherRepositoryMock()
        sut = SearchViewModel(mockWeatherRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState is Idle initially`() {
        assertEquals(Result.Idle, sut.uiState)
    }

    @Test
    fun `onSearchQueryChange updates searchQuery`() {
        // Given
        val query = "Test"

        // When
        sut.onSearchQueryChange(query)

        // Then
        assertEquals(query, sut.searchQuery)
    }

    @Test
    fun `onSearchTriggered does not search if query is too short`() {
        // When
        sut.onSearchTriggered("Te")

        // Then
        assertEquals(Result.Idle, sut.uiState)
    }

    @Test
    fun `onSearchTriggered searches if query is long enough`() = runTest {
        // Given
        val query = "Test"
        val expectedCities = CitiesStub.cities
        mockWeatherRepository.stubSearchCityResponse = expectedCities

        // When
        sut.onSearchTriggered(query)

        // Then
        assertEquals(Result.Success(expectedCities), sut.uiState)
    }

    @Test
    fun `searchCity returns Success with results`() = runTest {
        // Given
        val query = "Test"
        val expectedCities = CitiesStub.cities
        mockWeatherRepository.stubSearchCityResponse = expectedCities

        // When
        sut.searchCity(query)

        // Then
        assertEquals(Result.Success(expectedCities), sut.uiState)
    }

    @Test
    fun `searchCity returns NoResults when no results`() = runTest {
        // Given
        val query = "Test"
        mockWeatherRepository.stubSearchCityResponse = emptyList()

        // When
        sut.searchCity(query)

        // Then
        assertEquals(Result.NoResults, sut.uiState)
    }

    @Test
    fun `searchCity returns Error when exception is thrown`() = runTest {
        // Given
        val query = "Test"
        val exception = IOException("Network error")
        mockWeatherRepository.didSearchCity = { throw exception }

        // When
        sut.searchCity(query)

        // Then
        assertEquals(Result.Error(exception), sut.uiState)
    }

    @Test
    fun `retry searches if query is long enough`() = runTest {
        // Given
        val query = "Test"
        val expectedCities = CitiesStub.cities
        mockWeatherRepository.stubSearchCityResponse = expectedCities

        // When
        sut.onSearchQueryChange(query)
        sut.retry()

        // Then
        assertEquals(Result.Success(expectedCities), sut.uiState)
    }

    @Test
    fun `retry sets uiState to Idle if query is too short`() {
        // Given
        sut.onSearchQueryChange("Te")

        // When
        sut.retry()

        // Then
        assertEquals(Result.Idle, sut.uiState)
    }
}