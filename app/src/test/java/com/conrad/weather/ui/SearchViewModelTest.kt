package com.conrad.weather.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.conrad.weather.repository.WeatherRepositoryMock
import com.conrad.weather.ui.search.SearchViewModel
import com.conrad.weather.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()

    private lateinit var weatherRepository: WeatherRepositoryMock
    private lateinit var sut: SearchViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        weatherRepository = WeatherRepositoryMock()
        sut = SearchViewModel(weatherRepository)
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

        // When
        sut.onSearchTriggered(query)

        // Then
        assertNotEquals(Result.Idle, sut.uiState)
    }

    @Test
    fun `retry searches if query is long enough`() = runTest {
        // Given
        val query = "Test"

        // When
        sut.onSearchQueryChange(query)
        sut.retry()

        // Then
        assertNotEquals(Result.Idle, sut.uiState)
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