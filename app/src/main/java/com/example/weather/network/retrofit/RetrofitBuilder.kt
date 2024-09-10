package com.example.weather.network.retrofit

import com.example.weather.BuildConfig
import com.example.weather.network.other.GsonConverterProvider
import okhttp3.OkHttpClient
import retrofit2.Retrofit


object RetrofitBuilder {

    fun build(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
//            .addConverterFactory(JsonConverterFactoryProvider.instance)
            .addConverterFactory(GsonConverterProvider.instance)
            .build()
    }
}