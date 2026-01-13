package com.conrad.weather.di

import com.conrad.weather.repository.WeatherRepository
import com.conrad.weather.repository.WeatherRepositoryMock
import com.conrad.weather.local.settings.SettingsRepository
import com.conrad.weather.local.settings.DataStoreSettingsRepositoryMock
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MockAppModule {

    @Provides
    @Singleton
    fun provideWeatherRepository(): WeatherRepository {
        return WeatherRepositoryMock()
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(): SettingsRepository {
        return DataStoreSettingsRepositoryMock()
    }
}
