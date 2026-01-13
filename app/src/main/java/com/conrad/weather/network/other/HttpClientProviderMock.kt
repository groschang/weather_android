package com.conrad.weather.network.other

import okhttp3.OkHttpClient

object HttpClientProviderMock {

    private val interceptorMock: InterceptorMock by lazy {
        InterceptorMock()
    }

    val instance: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(interceptorMock)
            .build()
    }
}