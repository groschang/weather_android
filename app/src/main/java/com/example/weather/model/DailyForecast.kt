package com.example.weather.model

import com.google.gson.annotations.SerializedName
import java.util.Date


data class DailyForecast(
    @SerializedName("Date") val date: Date,
    @SerializedName("EpochDate") val epochDate: Int,
    @SerializedName("Temperature") val temperature: Temperature,
    @SerializedName("Day") val day: Day,
    @SerializedName("Night") val night: Night,
    @SerializedName("Sources") val sources: List<String>,
    @SerializedName("MobileLink") val mobileLink: String,
    @SerializedName("Link") val link: String
)