package com.conrad.weather.model

import com.google.gson.annotations.SerializedName

data class Night(
    @SerializedName("Icon") val icon: Int,
    @SerializedName("IconPhrase") val iconPhrase: String,
    @SerializedName("HasPrecipitation") val hasPrecipitation: Boolean,
    @SerializedName("PrecipitationType") val precipitationType: String? = null,
    @SerializedName("PrecipitationIntensity") val precipitationIntensity: String? = null
)