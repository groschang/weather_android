package com.conrad.weather.network.retrofit

import com.conrad.weather.BuildConfig
import com.conrad.weather.network.other.GsonConverterProvider
import okhttp3.OkHttpClient
import retrofit2.Retrofit


object RetrofitBuilder {

    fun build(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterProvider.instance)
            .build()
    }
}