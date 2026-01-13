package com.conrad.weather.di

import android.content.Context
import com.conrad.weather.local.cities.database.CitiesDatabase
import com.conrad.weather.local.cities.repository.LocalRepository
import com.conrad.weather.local.cities.repository.WeatherLocalRepository
import com.conrad.weather.local.settings.DataStoreSettingsRepository
import com.conrad.weather.local.settings.SettingsRepository
import com.conrad.weather.local.settings.dataStore
import com.conrad.weather.network.retrofit.RetrofitProvider
import com.conrad.weather.repository.NetworkWeatherRepository
import com.conrad.weather.repository.WeatherRepository
import com.conrad.weather.network.repository.WeatherRemoteDataSource
import com.conrad.weather.network.repository.WeatherDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProdAppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return RetrofitProvider.instance
    }

    @Provides
    @Singleton
    fun provideWeatherDataSource(retrofit: Retrofit): WeatherDataSource {
        return retrofit.create(WeatherRemoteDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideCitiesDatabase(@ApplicationContext context: Context): CitiesDatabase {
        return CitiesDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideLocalRepository(database: CitiesDatabase): LocalRepository {
        return WeatherLocalRepository(database.cityDao())
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(dataSource: WeatherDataSource, localRepository: LocalRepository): WeatherRepository {
        return NetworkWeatherRepository(
            dataSource = dataSource,
            database = localRepository
        )
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(@ApplicationContext context: Context): SettingsRepository {
        return DataStoreSettingsRepository(context.dataStore)
    }
}
