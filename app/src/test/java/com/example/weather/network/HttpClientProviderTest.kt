package com.example.weather.network

import com.example.weather.network.other.HttpClientProvider
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
import org.junit.Test

class HttpClientProviderTest {

    @Test
    fun `instance returns non null OkHttp client`() {
        // When
        val client = HttpClientProvider.instance

        // Then
        assertNotNull(client)
    }

    @Test
    fun `instance returns same instance`() {
        // When
        val instance1 = HttpClientProvider.instance
        val instance2 = HttpClientProvider.instance

        // Then
        assertSame(instance1, instance2)
    }

    @Test
    fun `instance has logging interceptor`() {
        // Given
        val client = HttpClientProvider.instance

        // When
        val interceptor = client.interceptors.find { it is HttpLoggingInterceptor }

        // Then
        assertNotNull(interceptor)
        assertEquals(
            HttpLoggingInterceptor.Level.BODY,
            (interceptor as HttpLoggingInterceptor).level
        )
    }
}