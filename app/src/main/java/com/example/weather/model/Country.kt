package com.example.weather.model

import com.google.gson.annotations.SerializedName


data class Country(
    @SerializedName("LocalizedName") val name: String
)