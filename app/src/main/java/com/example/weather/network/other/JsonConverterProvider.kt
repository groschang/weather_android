package com.example.weather.network.other

//import kotlinx.serialization.json.Json
//import okhttp3.MediaType.Companion.toMediaType
//import retrofit2.Converter
//import retrofit2.converter.kotlinx.serialization.asConverterFactory
//
//
//object JsonConverterProvider {
//
//    private val jsonFormat by lazy {
//        Json {
//            isLenient = true
//            ignoreUnknownKeys = true
//        }
//    }
//
//    val instance: Converter.Factory by lazy {
//        jsonFormat.asConverterFactory("application/json".toMediaType())
//    }
//
//}