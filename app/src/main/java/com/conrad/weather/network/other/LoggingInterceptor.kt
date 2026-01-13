package com.conrad.weather.network

import okhttp3.logging.HttpLoggingInterceptor

object LoggingInterceptor {
    val instance: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
}