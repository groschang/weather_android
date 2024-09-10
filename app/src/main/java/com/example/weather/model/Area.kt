package com.example.weather.model

import com.google.gson.annotations.SerializedName


data class Area(
    @SerializedName("LocalizedName") val name: String
)