package com.conrad.weather.model.stubs

import com.conrad.weather.model.DailyForecast
import com.conrad.weather.model.Day
import com.conrad.weather.model.Headline
import com.conrad.weather.model.Maximum
import com.conrad.weather.model.Minimum
import com.conrad.weather.model.Night
import com.conrad.weather.model.Temperature
import com.conrad.weather.model.WeatherForecast
import java.time.ZonedDateTime
import java.util.Collections
import java.util.Date

object WeatherForecastStub {

    val headline = Headline(
        effectiveDate = ZonedDateTime.parse("2024-09-06T07:00:00+01:00").toInstant().let {
            Date.from(it)
        },
        effectiveEpochDate = 1725714000,
        severity = 3,
        text = "Expect showery weather Saturday afternoon through Saturday evening",
        category = "rain",
        endDate = ZonedDateTime.parse("2024-09-08T02:00:00+01:00").toInstant().let {
            Date.from(it)
        },
        endEpochDate = 1725757200,
        mobileLink = "http://www.accuweather.com/en/gb/london/ec4a-2/daily-weather-forecast/328328?lang=en-us",
        link = "http://www.accuweather.com/en/gb/london/ec4a-2/daily-weather-forecast/328328?lang=en-us"
    )

    val dailyForecast = DailyForecast(
        date = ZonedDateTime.parse("2024-09-06T07:00:00+01:00").toInstant().let {
            Date.from(it)
        },
        epochDate = 1725602400,
        temperature = Temperature(
            minimum = Minimum(value = 56, unit = "F", unitType = 18),
            maximum = Maximum(value = 72, unit = "F", unitType = 18)
        ),
        day = Day(
            icon = 12,
            iconPhrase = "Showers",
            hasPrecipitation = true,
            precipitationType = "Rain",
            precipitationIntensity = "Light"
        ),
        night = Night(
            icon = 38,
            iconPhrase = "Mostly cloudy weather with some showers",
            hasPrecipitation = false
        ),
        sources = listOf("AccuWeather"),
        mobileLink = "http://www.accuweather.com/en/gb/london/ec4a-2/daily-weather-forecast/328328?day=1&lang=en-us",
        link = "http://www.accuweather.com/en/gb/london/ec4a-2/daily-weather-forecast/328328?day=1&lang=en-us"
    )

    val weatherForecast = WeatherForecast(
        headline = headline,
        dailyForecasts = Collections.nCopies(5, dailyForecast)
    )
}