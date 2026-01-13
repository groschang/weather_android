package com.conrad.weather.model

import com.google.gson.annotations.SerializedName

data class Temperature(
    @SerializedName("Minimum") val minimum: Minimum,
    @SerializedName("Maximum") val maximum: Maximum
) {
    override fun toString(): String {
        return "$minimum/$maximum"
    }
}

fun Temperature.magnitude() = maximum.value - minimum.value
