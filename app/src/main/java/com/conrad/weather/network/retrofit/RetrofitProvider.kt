package com.conrad.weather.network.retrofit

import com.conrad.weather.network.other.HttpClientProvider
import retrofit2.Retrofit


object RetrofitProvider {

    val instance: Retrofit by lazy {
        RetrofitBuilder.build(HttpClientProvider.instance)
    }
}