package com.conrad.weather.model

import com.google.gson.annotations.SerializedName

data class WeatherForecast(
    @SerializedName("Headline") val headline: Headline,
    @SerializedName("DailyForecasts") val dailyForecasts: List<DailyForecast>
)