package com.conrad.weather.network

import com.conrad.weather.database.LocalRepositoryMock
import com.conrad.weather.model.City
import com.conrad.weather.model.stubs.CitiesStub
import com.conrad.weather.model.stubs.WeatherForecastStub
import com.conrad.weather.repository.NetworkWeatherRepository

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class NetworkWeatherRepositoryTest {

    private lateinit var sut: NetworkWeatherRepository
    private lateinit var mockWeatherDataSource: WeatherDataSourceMock
    private lateinit var mockCitiesRepository: LocalRepositoryMock

    @Before
    fun setUp() {
        mockWeatherDataSource = WeatherDataSourceMock()
        mockCitiesRepository = LocalRepositoryMock()
        sut = NetworkWeatherRepository(mockWeatherDataSource, mockCitiesRepository)
    }

    @Test
    fun `searchCity calls weatherDataSource with correct parameters`() = runBlocking {
        // Given
        val expectedCities = CitiesStub.cities
        val searchPhrase = "Phrase"
        mockWeatherDataSource.stubSearchCityResponse = expectedCities

        // When
        val cities = sut.searchCity(searchPhrase)

        // Then
        assertEquals(1, mockWeatherDataSource.searchCityCallCount)
        assertEquals(expectedCities, cities)
    }

    @Test
    fun `searchCity calls weatherDataSource with incorrect parameters`() = runBlocking {
        // Given
        val expectedCities: List<City> = listOf()
        val searchPhrase = ""
        mockWeatherDataSource.stubSearchCityResponse = expectedCities

        // When
        val cities = sut.searchCity(searchPhrase)

        // Then
        assertEquals(1, mockWeatherDataSource.searchCityCallCount)
        assertEquals(expectedCities, cities)
    }

    @Test
    fun `fetchForecast calls weatherDataSource with correct parameters`() = runBlocking {
        // Given
        val expectedForecast = WeatherForecastStub.weatherForecast
        val locationKey = "123"
        mockWeatherDataSource.stubForecastResponse = expectedForecast

        // When
        val forecast = sut.fetchForecast(locationKey)

        // Then
        assertEquals(1, mockWeatherDataSource.forecastCallCount)
        assertEquals(expectedForecast, forecast)
    }

    @Test
    fun `storeCity calls citiesRepository with correct parameters`() = runBlocking {
        // Given
        val city = CitiesStub.city

        // When
        sut.storeCity(city)

        // Then
        assertEquals(1, mockCitiesRepository.insertCityCallCount)
    }

    @Test
    fun `getCity calls citiesRepository with correct parameters`() = runBlocking {
        // Given
        val expectedCity = CitiesStub.city
        mockCitiesRepository.stubGetCityStreamResponse = flowOf(expectedCity)

        // When
        val city = sut.getCity(expectedCity.id)

        // Then
        assertEquals(1, mockCitiesRepository.getCityStreamCallCount)
        assertEquals(expectedCity, city.first())
    }

    @Test
    fun `getCity calls citiesRepository with incorrect parameters`() = runBlocking {
        // Given
        val incorrectId = "123"
        val expectedCity = null
        mockCitiesRepository.stubGetCityStreamResponse = flowOf(null)

        // When
        val city = sut.getCity(incorrectId)

        // Then
        assertEquals(1, mockCitiesRepository.getCityStreamCallCount)
        assertEquals(expectedCity, city.first())
    }

    @Test
    fun `getCities calls citiesRepository with correct parameters`() = runBlocking {
        // Given
        val expectedCities = CitiesStub.cities
        mockCitiesRepository.stubGetAllCitiesStreamResponse = flowOf(expectedCities)

        // When
        val cities = sut.getCities()

        // Then
        assertEquals(1, mockCitiesRepository.getAllCitiesStreamCallCount)
        assertEquals(expectedCities, cities.first())
    }

    @Test
    fun `deleteCity calls citiesRepository with correct parameters`() = runBlocking {
        // Given
        val city = CitiesStub.city

        // When
        sut.deleteCity(city.id)

        // Then
        assertEquals(1, mockCitiesRepository.deleteCityCallCount)
    }

}