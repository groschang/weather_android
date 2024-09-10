package com.example.weather.network.other

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


object HttpClientProvider {

    private val loggingInterceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    val instance: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }
}