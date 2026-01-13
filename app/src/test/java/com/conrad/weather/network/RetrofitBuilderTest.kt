package com.conrad.weather.network

import com.conrad.weather.BuildConfig
import com.conrad.weather.network.retrofit.RetrofitBuilder

import retrofit2.converter.gson.GsonConverterFactory

import okhttp3.OkHttpClient

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class RetrofitBuilderTest {

    private lateinit var okHttpClient: OkHttpClient

    @Before
    fun setUp() {
        okHttpClient = OkHttpClient.Builder().build()
    }

    @Test
    fun `build returns non nullR Retrofit instance`() {
        // When
        val retrofit = RetrofitBuilder.build(okHttpClient)

        // Then
        assertNotNull(retrofit)
    }

    @Test
    fun `build sets correct base url`() {
        // When
        val retrofit = RetrofitBuilder.build(okHttpClient)

        // Then
        assertEquals(
            BuildConfig.BASE_URL,
            retrofit.baseUrl().toString().dropLast(1)
        )
    }

    @Test
    fun `build sets correct OkHttpClient`() {
        // When
        val retrofit = RetrofitBuilder.build(okHttpClient)

        // Then
        assertEquals(okHttpClient, retrofit.callFactory())
    }

    @Test
    fun `build sets correct  converter factory`() {
        // Given
        val retrofit = RetrofitBuilder.build(okHttpClient)

        // When
        val converterFactory = retrofit.converterFactories().find { it is GsonConverterFactory }

        // Then
        assertNotNull(converterFactory)
    }
}