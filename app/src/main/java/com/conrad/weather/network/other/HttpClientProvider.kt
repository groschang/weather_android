package com.conrad.weather.network.other

import com.conrad.weather.network.AuthInterceptor
import com.conrad.weather.network.LoggingInterceptor
import okhttp3.OkHttpClient

object HttpClientProvider {

    val instance: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(LoggingInterceptor.instance)
            .addInterceptor(AuthInterceptor())
            .build()
    }
}