package com.conrad.weather.network.retrofit

import com.conrad.weather.network.other.HttpClientProviderMock
import retrofit2.Retrofit


object MockRetrofitProvider {

    val instance: Retrofit by lazy {
        RetrofitBuilder.build(HttpClientProviderMock.instance)
    }
}