package com.conrad.weather.model

import com.google.gson.annotations.SerializedName

data class Maximum(
    @SerializedName("Value") val value: Int,
    @SerializedName("Unit") val unit: String,
    @SerializedName("UnitType") val unitType: Int
) {
    override fun toString(): String {
        return "$value$unit"
    }
}
