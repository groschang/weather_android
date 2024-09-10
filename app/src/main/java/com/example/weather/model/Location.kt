package com.example.weather.model

import com.google.gson.annotations.SerializedName


data class Location(
    @SerializedName("Latitude") val latitude: Double,
    @SerializedName("Longitude") val longitude: Double
)