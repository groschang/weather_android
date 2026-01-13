package com.conrad.weather.network.other

import com.conrad.weather.utils.DateFormatter
import com.google.gson.GsonBuilder
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory

object GsonConverterProvider {

    private val gson by lazy {
        GsonBuilder()
            .setDateFormat(DateFormatter.DATE_FORMAT)
            .create()
    }

    val instance: Converter.Factory by lazy {
        GsonConverterFactory.create(gson)
    }

}