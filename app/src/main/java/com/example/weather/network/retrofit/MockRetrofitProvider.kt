package com.example.weather.network.retrofit

import com.example.weather.network.other.HttpClientProviderMock
import retrofit2.Retrofit


object MockRetrofitProvider {

    val instance: Retrofit by lazy {
        RetrofitBuilder.build(HttpClientProviderMock.instance)
    }
}