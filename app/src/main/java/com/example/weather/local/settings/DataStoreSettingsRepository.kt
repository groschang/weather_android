package com.example.weather.local.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


const val USER_PREFERENCES_NAME = "user_preferences"

class DataStoreSettingsRepository(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    private companion object {
        val USER_NAME = stringPreferencesKey("city_id")
    }

    override val currentCityId: Flow<String?> =
        dataStore.data.map { preferences ->
            val value = preferences[USER_NAME] ?: ""
            value.ifBlank { null }
        }

    override suspend fun saveCurrentCityId(cityId: String?) {
        dataStore.edit { preferences ->
            preferences[USER_NAME] = cityId ?: ""
        }
    }
}

val Context.dataStore by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)