package com.example.weather.local

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesOf
import com.example.weather.local.settings.DataStoreSettingsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DataStoreSettingsRepositoryTest {

    private lateinit var mockDataStore: DataStoreMock<Preferences>
    private lateinit var sut: DataStoreSettingsRepository

    @Before
    fun setup() {
        mockDataStore = DataStoreMock(emptyPreferences())
        sut = DataStoreSettingsRepository(mockDataStore)
    }

    @Test
    fun `currentCityId returns null when no cityId is saved`() = runBlocking {
        // Given
        val expectedCityId = null

        // When
        val cityId = sut.currentCityId

        // Then
        assertEquals(expectedCityId, cityId.first())
    }

    @Test
    fun `currentCityId returns saved cityId`() = runBlocking {
        // Given
        val expectedCityId = "123"
        val preferences = preferencesOf(DataStoreSettingsRepository.USER_NAME to expectedCityId)
        mockDataStore.stubDataFlowResponse.value = preferences

        // When
        val cityId = sut.currentCityId

        // Then
        assertEquals(expectedCityId, cityId.first())
    }

    @Test
    fun `saveCurrentCityId saves cityId to DataStore`() = runBlocking {
        // Given
        val expectedCityId = "123"

        // When
        sut.saveCurrentCityId(expectedCityId)

        // Then
        assertEquals(expectedCityId, sut.currentCityId.first())
    }

    @Test
    fun `saveCurrentCityId saves empty when cityId is null`() = runBlocking {
        // Given
        val expectedCityId = null

        // When
        sut.saveCurrentCityId(expectedCityId)

        // Then
        assertEquals(expectedCityId, sut.currentCityId.first())
    }
}