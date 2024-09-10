package com.example.weather.model

import com.google.gson.annotations.SerializedName
import java.util.Date


data class Headline(
    @SerializedName("EffectiveDate") val effectiveDate: Date,
    @SerializedName("EffectiveEpochDate") val effectiveEpochDate: Int,
    @SerializedName("Severity") val severity: Int,
    @SerializedName("Text") val text: String,
    @SerializedName("Category") val category: String,
    @SerializedName("EndDate") val endDate: Date,
    @SerializedName("EndEpochDate") val endEpochDate: Int,
    @SerializedName("MobileLink") val mobileLink: String,
    @SerializedName("Link") val link: String
)