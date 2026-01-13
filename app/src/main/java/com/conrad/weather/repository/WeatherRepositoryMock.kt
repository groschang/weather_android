package com.conrad.weather.repository

import com.conrad.weather.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date

class WeatherRepositoryMock : WeatherRepository {
    
    // Enhanced mock data with more realistic cities and geo-positioning
    private val mockCities = listOf(
        City(
            id = "345119",
            name = "New York",
            position = Location(
                latitude = 40.7128,
                longitude = -74.0059
            ),
            country = Country("United States"),
            area = Area("NY")
        ),
        City(
            id = "328328",
            name = "London",
            position = Location(
                latitude = 51.5074,
                longitude = -0.1278
            ),
            country = Country("United Kingdom"),
            area = Area("England")
        ),
        City(
            id = "226396",
            name = "Tokyo",
            position = Location(
                latitude = 35.6895,
                longitude = 139.6917
            ),
            country = Country("Japan"),
            area = Area("Tokyo")
        ),
        City(
            id = "298824",
            name = "Paris",
            position = Location(
                latitude = 48.8566,
                longitude = 2.3522
            ),
            country = Country("France"),
            area = Area("Île-de-France")
        ),
        City(
            id = "318251",
            name = "Sydney",
            position = Location(
                latitude = -33.8688,
                longitude = 151.2093
            ),
            country = Country("Australia"),
            area = Area("New South Wales")
        ),
        City(
            id = "219373",
            name = "Rome",
            position = Location(
                latitude = 41.9028,
                longitude = 12.4964
            ),
            country = Country("Italy"),
            area = Area("Lazio")
        ),
        City(
            id = "261842",
            name = "Copenhagen",
            position = Location(
                latitude = 55.6761,
                longitude = 12.5683
            ),
            country = Country("Denmark"),
            area = Area("Hovedstaden")
        )
    )

    // More detailed weather forecasts for different cities
    private val mockForecasts = mapOf(
        "345119" to createNewYorkForecast(), // New York
        "328328" to createLondonForecast(),  // London
        "226396" to createTokyoForecast(),   // Tokyo
        "298824" to createParisForecast(),   // Paris
        "318251" to createSydneyForecast(),  // Sydney
        "219373" to createRomeForecast(),    // Rome
        "261842" to createCopenhagenForecast() // Copenhagen
    )

    // Store for saved cities with London as default
    private val newYork = mockCities.find { it.id == "328328" }!!
    private val savedCities = MutableStateFlow<List<City>>(listOf(newYork))
    
    override suspend fun searchCity(query: String): List<City> {
        return mockCities.filter { 
            it.name.contains(query, ignoreCase = true) || 
            it.country?.name?.contains(query, ignoreCase = true) == true ||
            it.area?.name?.contains(query, ignoreCase = true) == true
        }
    }

    override suspend fun fetchForecast(locationKey: String): WeatherForecast? {
        return mockForecasts[locationKey]
    }

    override suspend fun storeCity(city: City) {
        val currentCities = savedCities.value.toMutableList()
        // Check if city already exists in saved cities
        val existingIndex = currentCities.indexOfFirst { it.id == city.id }
        if (existingIndex >= 0) {
            // Replace existing city with updated one
            currentCities[existingIndex] = city
        } else {
            // Add new city
            currentCities.add(city)
        }
        savedCities.value = currentCities
    }

    override suspend fun updateCity(city: City) {
        storeCity(city) // In mock, update is the same as store
    }

    override suspend fun getCity(id: String): Flow<City?> {
        // First check in saved cities, then fall back to mock cities
        val city = savedCities.value.find { it.id == id } ?: mockCities.find { it.id == id }
        return MutableStateFlow(city)
    }

    override suspend fun getCities(): Flow<List<City>> {
        return savedCities.asStateFlow()
    }

    override suspend fun deleteCity(id: String) {
        val currentCities = savedCities.value.toMutableList()
        val cityToRemove = currentCities.find { it.id == id }
        if (cityToRemove != null) {
            currentCities.remove(cityToRemove)
            savedCities.value = currentCities
        }
    }
    
    // Helper functions to create detailed weather forecasts
    private fun createNewYorkForecast(): WeatherForecast {
        val now = Date()
        val tomorrow = Date(now.time + 24 * 60 * 60 * 1000)
        val dayAfterTomorrow = Date(now.time + 48 * 60 * 60 * 1000)
        val fourthDay = Date(now.time + 72 * 60 * 60 * 1000)
        val fifthDay = Date(now.time + 96 * 60 * 60 * 1000)

        return WeatherForecast(
            headline = Headline(
                effectiveDate = now,
                effectiveEpochDate = (now.time / 1000).toInt(),
                severity = 3,
                text = "Expect sunny conditions with clear skies throughout the day",
                category = "sunny",
                endDate = fifthDay,
                endEpochDate = (fifthDay.time / 1000).toInt(),
                mobileLink = "http://www.accuweather.com/en/us/new-york-ny/10001/daily-weather-forecast/345119",
                link = "http://www.accuweather.com/en/us/new-york-ny/10001/daily-weather-forecast/345119"
            ),
            dailyForecasts = listOf(
                DailyForecast(
                    date = now,
                    epochDate = (now.time / 1000).toInt(),
                    temperature = Temperature(
                        minimum = Minimum(
                            value = 18,
                            unit = "C",
                            unitType = 17
                        ),
                        maximum = Maximum(
                            value = 25,
                            unit = "C",
                            unitType = 17
                        )
                    ),
                    day = Day(
                        icon = 1,
                        iconPhrase = "Sunny",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    night = Night(
                        icon = 33,
                        iconPhrase = "Clear",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    sources = listOf("AccuWeather"),
                    mobileLink = "http://www.accuweather.com/en/us/new-york-ny/10001/daily-weather-forecast/345119?day=1",
                    link = "http://www.accuweather.com/en/us/new-york-ny/10001/daily-weather-forecast/345119?day=1"
                ),
                DailyForecast(
                    date = tomorrow,
                    epochDate = (tomorrow.time / 1000).toInt(),
                    temperature = Temperature(
                        minimum = Minimum(
                            value = 19,
                            unit = "C",
                            unitType = 17
                        ),
                        maximum = Maximum(
                            value = 26,
                            unit = "C",
                            unitType = 17
                        )
                    ),
                    day = Day(
                        icon = 2,
                        iconPhrase = "Mostly sunny",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    night = Night(
                        icon = 34,
                        iconPhrase = "Mostly clear",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    sources = listOf("AccuWeather"),
                    mobileLink = "http://www.accuweather.com/en/us/new-york-ny/10001/daily-weather-forecast/345119?day=2",
                    link = "http://www.accuweather.com/en/us/new-york-ny/10001/daily-weather-forecast/345119?day=2"
                ),
                DailyForecast(
                    date = dayAfterTomorrow,
                    epochDate = (dayAfterTomorrow.time / 1000).toInt(),
                    temperature = Temperature(
                        minimum = Minimum(
                            value = 20,
                            unit = "C",
                            unitType = 17
                        ),
                        maximum = Maximum(
                            value = 27,
                            unit = "C",
                            unitType = 17
                        )
                    ),
                    day = Day(
                        icon = 4,
                        iconPhrase = "Partly sunny",
                        hasPrecipitation = true,
                        precipitationType = "Rain",
                        precipitationIntensity = "Light"
                    ),
                    night = Night(
                        icon = 36,
                        iconPhrase = "Partly cloudy",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    sources = listOf("AccuWeather"),
                    mobileLink = "http://www.accuweather.com/en/us/new-york-ny/10001/daily-weather-forecast/345119?day=3",
                    link = "http://www.accuweather.com/en/us/new-york-ny/10001/daily-weather-forecast/345119?day=3"
                ),
                DailyForecast(
                    date = fourthDay,
                    epochDate = (fourthDay.time / 1000).toInt(),
                    temperature = Temperature(
                        minimum = Minimum(
                            value = 21,
                            unit = "C",
                            unitType = 17
                        ),
                        maximum = Maximum(
                            value = 28,
                            unit = "C",
                            unitType = 17
                        )
                    ),
                    day = Day(
                        icon = 3,
                        iconPhrase = "Partly sunny",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    night = Night(
                        icon = 35,
                        iconPhrase = "Partly cloudy",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    sources = listOf("AccuWeather"),
                    mobileLink = "http://www.accuweather.com/en/us/new-york-ny/10001/daily-weather-forecast/345119?day=4",
                    link = "http://www.accuweather.com/en/us/new-york-ny/10001/daily-weather-forecast/345119?day=4"
                ),
                DailyForecast(
                    date = fifthDay,
                    epochDate = (fifthDay.time / 1000).toInt(),
                    temperature = Temperature(
                        minimum = Minimum(
                            value = 22,
                            unit = "C",
                            unitType = 17
                        ),
                        maximum = Maximum(
                            value = 29,
                            unit = "C",
                            unitType = 17
                        )
                    ),
                    day = Day(
                        icon = 1,
                        iconPhrase = "Sunny",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    night = Night(
                        icon = 33,
                        iconPhrase = "Clear",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    sources = listOf("AccuWeather"),
                    mobileLink = "http://www.accuweather.com/en/us/new-york-ny/10001/daily-weather-forecast/345119?day=5",
                    link = "http://www.accuweather.com/en/us/new-york-ny/10001/daily-weather-forecast/345119?day=5"
                )
            )
        )
    }
    
    private fun createLondonForecast(): WeatherForecast {
        val now = Date()
        val tomorrow = Date(now.time + 24 * 60 * 60 * 1000)
        val dayAfterTomorrow = Date(now.time + 48 * 60 * 60 * 1000)
        val fourthDay = Date(now.time + 72 * 60 * 60 * 1000)
        val fifthDay = Date(now.time + 96 * 60 * 60 * 1000)

        return WeatherForecast(
            headline = Headline(
                effectiveDate = now,
                effectiveEpochDate = (now.time / 1000).toInt(),
                severity = 4,
                text = "Expect overcast conditions with periods of light rain",
                category = "rain",
                endDate = fifthDay,
                endEpochDate = (fifthDay.time / 1000).toInt(),
                mobileLink = "http://www.accuweather.com/en/gb/london/ec4a-2/daily-weather-forecast/328328",
                link = "http://www.accuweather.com/en/gb/london/ec4a-2/daily-weather-forecast/328328"
            ),
            dailyForecasts = listOf(
                DailyForecast(
                    date = now,
                    epochDate = (now.time / 1000).toInt(),
                    temperature = Temperature(
                        minimum = Minimum(
                            value = 11,
                            unit = "C",
                            unitType = 17
                        ),
                        maximum = Maximum(
                            value = 18,
                            unit = "C",
                            unitType = 17
                        )
                    ),
                    day = Day(
                        icon = 12,
                        iconPhrase = "Showers",
                        hasPrecipitation = true,
                        precipitationType = "Rain",
                        precipitationIntensity = "Light"
                    ),
                    night = Night(
                        icon = 39,
                        iconPhrase = "Partly cloudy w/ showers",
                        hasPrecipitation = true,
                        precipitationType = "Rain",
                        precipitationIntensity = "Light"
                    ),
                    sources = listOf("AccuWeather"),
                    mobileLink = "http://www.accuweather.com/en/gb/london/ec4a-2/daily-weather-forecast/328328?day=1",
                    link = "http://www.accuweather.com/en/gb/london/ec4a-2/daily-weather-forecast/328328?day=1"
                ),
                DailyForecast(
                    date = tomorrow,
                    epochDate = (tomorrow.time / 1000).toInt(),
                    temperature = Temperature(
                        minimum = Minimum(
                            value = 11,
                            unit = "C",
                            unitType = 17
                        ),
                        maximum = Maximum(
                            value = 16,
                            unit = "C",
                            unitType = 17
                        )
                    ),
                    day = Day(
                        icon = 6,
                        iconPhrase = "Mostly cloudy",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    night = Night(
                        icon = 38,
                        iconPhrase = "Mostly cloudy",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    sources = listOf("AccuWeather"),
                    mobileLink = "http://www.accuweather.com/en/gb/london/ec4a-2/daily-weather-forecast/328328?day=2",
                    link = "http://www.accuweather.com/en/gb/london/ec4a-2/daily-weather-forecast/328328?day=2"
                ),
                DailyForecast(
                    date = dayAfterTomorrow,
                    epochDate = (dayAfterTomorrow.time / 1000).toInt(),
                    temperature = Temperature(
                        minimum = Minimum(
                            value = 13,
                            unit = "C",
                            unitType = 17
                        ),
                        maximum = Maximum(
                            value = 18,
                            unit = "C",
                            unitType = 17
                        )
                    ),
                    day = Day(
                        icon = 7,
                        iconPhrase = "Cloudy",
                        hasPrecipitation = true,
                        precipitationType = "Rain",
                        precipitationIntensity = "Moderate"
                    ),
                    night = Night(
                        icon = 40,
                        iconPhrase = "Cloudy w/ showers",
                        hasPrecipitation = true,
                        precipitationType = "Rain",
                        precipitationIntensity = "Moderate"
                    ),
                    sources = listOf("AccuWeather"),
                    mobileLink = "http://www.accuweather.com/en/gb/london/ec4a-2/daily-weather-forecast/328328?day=3",
                    link = "http://www.accuweather.com/en/gb/london/ec4a-2/daily-weather-forecast/328328?day=3"
                ),
                DailyForecast(
                    date = fourthDay,
                    epochDate = (fourthDay.time / 1000).toInt(),
                    temperature = Temperature(
                        minimum = Minimum(
                            value = 14,
                            unit = "C",
                            unitType = 17
                        ),
                        maximum = Maximum(
                            value = 19,
                            unit = "C",
                            unitType = 17
                        )
                    ),
                    day = Day(
                        icon = 5,
                        iconPhrase = "Partly cloudy",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    night = Night(
                        icon = 37,
                        iconPhrase = "Partly cloudy",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    sources = listOf("AccuWeather"),
                    mobileLink = "http://www.accuweather.com/en/gb/london/ec4a-2/daily-weather-forecast/328328?day=4",
                    link = "http://www.accuweather.com/en/gb/london/ec4a-2/daily-weather-forecast/328328?day=4"
                ),
                DailyForecast(
                    date = fifthDay,
                    epochDate = (fifthDay.time / 1000).toInt(),
                    temperature = Temperature(
                        minimum = Minimum(
                            value = 15,
                            unit = "C",
                            unitType = 17
                        ),
                        maximum = Maximum(
                            value = 21,
                            unit = "C",
                            unitType = 17
                        )
                    ),
                    day = Day(
                        icon = 1,
                        iconPhrase = "Sunny",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    night = Night(
                        icon = 33,
                        iconPhrase = "Clear",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    sources = listOf("AccuWeather"),
                    mobileLink = "http://www.accuweather.com/en/gb/london/ec4a-2/daily-weather-forecast/328328?day=5",
                    link = "http://www.accuweather.com/en/gb/london/ec4a-2/daily-weather-forecast/328328?day=5"
                )
            )
        )
    }
    
    private fun createTokyoForecast(): WeatherForecast {
        val now = Date()
        val tomorrow = Date(now.time + 24 * 60 * 60 * 1000)
        val dayAfterTomorrow = Date(now.time + 48 * 60 * 60 * 1000)
        val fourthDay = Date(now.time + 72 * 60 * 60 * 1000)
        val fifthDay = Date(now.time + 96 * 60 * 60 * 1000)

        return WeatherForecast(
            headline = Headline(
                effectiveDate = now,
                effectiveEpochDate = (now.time / 1000).toInt(),
                severity = 2,
                text = "Expect mild temperatures with clear skies",
                category = "sunny",
                endDate = fifthDay,
                endEpochDate = (fifthDay.time / 1000).toInt(),
                mobileLink = "http://www.accuweather.com/en/jp/tokyo/1850147/daily-weather-forecast/226396",
                link = "http://www.accuweather.com/en/jp/tokyo/1850147/daily-weather-forecast/226396"
            ),
            dailyForecasts = listOf(
                DailyForecast(
                    date = now,
                    epochDate = (now.time / 1000).toInt(),
                    temperature = Temperature(
                        minimum = Minimum(
                            value = 22,
                            unit = "C",
                            unitType = 17
                        ),
                        maximum = Maximum(
                            value = 29,
                            unit = "C",
                            unitType = 17
                        )
                    ),
                    day = Day(
                        icon = 1,
                        iconPhrase = "Sunny",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    night = Night(
                        icon = 33,
                        iconPhrase = "Clear",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    sources = listOf("AccuWeather"),
                    mobileLink = "http://www.accuweather.com/en/jp/tokyo/1850147/daily-weather-forecast/226396?day=1",
                    link = "http://www.accuweather.com/en/jp/tokyo/1850147/daily-weather-forecast/226396?day=1"
                ),
                DailyForecast(
                    date = tomorrow,
                    epochDate = (tomorrow.time / 1000).toInt(),
                    temperature = Temperature(
                        minimum = Minimum(
                            value = 23,
                            unit = "C",
                            unitType = 17
                        ),
                        maximum = Maximum(
                            value = 30,
                            unit = "C",
                            unitType = 17
                        )
                    ),
                    day = Day(
                        icon = 2,
                        iconPhrase = "Mostly sunny",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    night = Night(
                        icon = 34,
                        iconPhrase = "Mostly clear",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    sources = listOf("AccuWeather"),
                    mobileLink = "http://www.accuweather.com/en/jp/tokyo/1850147/daily-weather-forecast/226396?day=2",
                    link = "http://www.accuweather.com/en/jp/tokyo/1850147/daily-weather-forecast/226396?day=2"
                ),
                DailyForecast(
                    date = dayAfterTomorrow,
                    epochDate = (dayAfterTomorrow.time / 1000).toInt(),
                    temperature = Temperature(
                        minimum = Minimum(
                            value = 21,
                            unit = "C",
                            unitType = 17
                        ),
                        maximum = Maximum(
                            value = 28,
                            unit = "C",
                            unitType = 17
                        )
                    ),
                    day = Day(
                        icon = 4,
                        iconPhrase = "Partly sunny",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    night = Night(
                        icon = 36,
                        iconPhrase = "Partly cloudy",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    sources = listOf("AccuWeather"),
                    mobileLink = "http://www.accuweather.com/en/jp/tokyo/1850147/daily-weather-forecast/226396?day=3",
                    link = "http://www.accuweather.com/en/jp/tokyo/1850147/daily-weather-forecast/226396?day=3"
                ),
                DailyForecast(
                    date = fourthDay,
                    epochDate = (fourthDay.time / 1000).toInt(),
                    temperature = Temperature(
                        minimum = Minimum(
                            value = 20,
                            unit = "C",
                            unitType = 17
                        ),
                        maximum = Maximum(
                            value = 27,
                            unit = "C",
                            unitType = 17
                        )
                    ),
                    day = Day(
                        icon = 6,
                        iconPhrase = "Mostly cloudy",
                        hasPrecipitation = true,
                        precipitationType = "Rain",
                        precipitationIntensity = "Light"
                    ),
                    night = Night(
                        icon = 38,
                        iconPhrase = "Mostly cloudy",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    sources = listOf("AccuWeather"),
                    mobileLink = "http://www.accuweather.com/en/jp/tokyo/1850147/daily-weather-forecast/226396?day=4",
                    link = "http://www.accuweather.com/en/jp/tokyo/1850147/daily-weather-forecast/226396?day=4"
                ),
                DailyForecast(
                    date = fifthDay,
                    epochDate = (fifthDay.time / 1000).toInt(),
                    temperature = Temperature(
                        minimum = Minimum(
                            value = 22,
                            unit = "C",
                            unitType = 17
                        ),
                        maximum = Maximum(
                            value = 30,
                            unit = "C",
                            unitType = 17
                        )
                    ),
                    day = Day(
                        icon = 1,
                        iconPhrase = "Sunny",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    night = Night(
                        icon = 33,
                        iconPhrase = "Clear",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    sources = listOf("AccuWeather"),
                    mobileLink = "http://www.accuweather.com/en/jp/tokyo/1850147/daily-weather-forecast/226396?day=5",
                    link = "http://www.accuweather.com/en/jp/tokyo/1850147/daily-weather-forecast/226396?day=5"
                )
            )
        )
    }
    
    private fun createParisForecast(): WeatherForecast {
        val now = Date()
        val tomorrow = Date(now.time + 24 * 60 * 60 * 1000)
        val dayAfterTomorrow = Date(now.time + 48 * 60 * 60 * 1000)
        val fourthDay = Date(now.time + 72 * 60 * 60 * 1000)
        val fifthDay = Date(now.time + 96 * 60 * 60 * 1000)

        return WeatherForecast(
            headline = Headline(
                effectiveDate = now,
                effectiveEpochDate = (now.time / 1000).toInt(),
                severity = 3,
                text = "Expect pleasant conditions with variable clouds",
                category = "partly-sunny",
                endDate = fifthDay,
                endEpochDate = (fifthDay.time / 1000).toInt(),
                mobileLink = "http://www.accuweather.com/en/fr/paris/75000/daily-weather-forecast/298824",
                link = "http://www.accuweather.com/en/fr/paris/75000/daily-weather-forecast/298824"
            ),
            dailyForecasts = listOf(
                DailyForecast(
                    date = now,
                    epochDate = (now.time / 1000).toInt(),
                    temperature = Temperature(
                        minimum = Minimum(
                            value = 15,
                            unit = "C",
                            unitType = 17
                        ),
                        maximum = Maximum(
                            value = 22,
                            unit = "C",
                            unitType = 17
                        )
                    ),
                    day = Day(
                        icon = 3,
                        iconPhrase = "Partly sunny",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    night = Night(
                        icon = 35,
                        iconPhrase = "Partly cloudy",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    sources = listOf("AccuWeather"),
                    mobileLink = "http://www.accuweather.com/en/fr/paris/75000/daily-weather-forecast/298824?day=1",
                    link = "http://www.accuweather.com/en/fr/paris/75000/daily-weather-forecast/298824?day=1"
                ),
                DailyForecast(
                    date = tomorrow,
                    epochDate = (tomorrow.time / 1000).toInt(),
                    temperature = Temperature(
                        minimum = Minimum(
                            value = 14,
                            unit = "C",
                            unitType = 17
                        ),
                        maximum = Maximum(
                            value = 21,
                            unit = "C",
                            unitType = 17
                        )
                    ),
                    day = Day(
                        icon = 6,
                        iconPhrase = "Mostly cloudy",
                        hasPrecipitation = true,
                        precipitationType = "Rain",
                        precipitationIntensity = "Light"
                    ),
                    night = Night(
                        icon = 38,
                        iconPhrase = "Mostly cloudy",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    sources = listOf("AccuWeather"),
                    mobileLink = "http://www.accuweather.com/en/fr/paris/75000/daily-weather-forecast/298824?day=2",
                    link = "http://www.accuweather.com/en/fr/paris/75000/daily-weather-forecast/298824?day=2"
                ),
                DailyForecast(
                    date = dayAfterTomorrow,
                    epochDate = (dayAfterTomorrow.time / 1000).toInt(),
                    temperature = Temperature(
                        minimum = Minimum(
                            value = 16,
                            unit = "C",
                            unitType = 17
                        ),
                        maximum = Maximum(
                            value = 23,
                            unit = "C",
                            unitType = 17
                        )
                    ),
                    day = Day(
                        icon = 1,
                        iconPhrase = "Sunny",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    night = Night(
                        icon = 33,
                        iconPhrase = "Clear",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    sources = listOf("AccuWeather"),
                    mobileLink = "http://www.accuweather.com/en/fr/paris/75000/daily-weather-forecast/298824?day=3",
                    link = "http://www.accuweather.com/en/fr/paris/75000/daily-weather-forecast/298824?day=3"
                ),
                DailyForecast(
                    date = fourthDay,
                    epochDate = (fourthDay.time / 1000).toInt(),
                    temperature = Temperature(
                        minimum = Minimum(
                            value = 17,
                            unit = "C",
                            unitType = 17
                        ),
                        maximum = Maximum(
                            value = 24,
                            unit = "C",
                            unitType = 17
                        )
                    ),
                    day = Day(
                        icon = 2,
                        iconPhrase = "Mostly sunny",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    night = Night(
                        icon = 34,
                        iconPhrase = "Mostly clear",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    sources = listOf("AccuWeather"),
                    mobileLink = "http://www.accuweather.com/en/fr/paris/75000/daily-weather-forecast/298824?day=4",
                    link = "http://www.accuweather.com/en/fr/paris/75000/daily-weather-forecast/298824?day=4"
                ),
                DailyForecast(
                    date = fifthDay,
                    epochDate = (fifthDay.time / 1000).toInt(),
                    temperature = Temperature(
                        minimum = Minimum(
                            value = 18,
                            unit = "C",
                            unitType = 17
                        ),
                        maximum = Maximum(
                            value = 25,
                            unit = "C",
                            unitType = 17
                        )
                    ),
                    day = Day(
                        icon = 4,
                        iconPhrase = "Partly sunny",
                        hasPrecipitation = true,
                        precipitationType = "Rain",
                        precipitationIntensity = "Light"
                    ),
                    night = Night(
                        icon = 36,
                        iconPhrase = "Partly cloudy",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    sources = listOf("AccuWeather"),
                    mobileLink = "http://www.accuweather.com/en/fr/paris/75000/daily-weather-forecast/298824?day=5",
                    link = "http://www.accuweather.com/en/fr/paris/75000/daily-weather-forecast/298824?day=5"
                )
            )
        )
    }
    
    private fun createSydneyForecast(): WeatherForecast {
        val now = Date()
        val tomorrow = Date(now.time + 24 * 60 * 60 * 1000)
        val dayAfterTomorrow = Date(now.time + 48 * 60 * 60 * 1000)
        val fourthDay = Date(now.time + 72 * 60 * 60 * 1000)
        val fifthDay = Date(now.time + 96 * 60 * 60 * 1000)

        return WeatherForecast(
            headline = Headline(
                effectiveDate = now,
                effectiveEpochDate = (now.time / 1000).toInt(),
                severity = 2,
                text = "Expect pleasant spring conditions with mild temperatures",
                category = "sunny",
                endDate = fifthDay,
                endEpochDate = (fifthDay.time / 1000).toInt(),
                mobileLink = "http://www.accuweather.com/en/au/sydney/2000/daily-weather-forecast/318251",
                link = "http://www.accuweather.com/en/au/sydney/2000/daily-weather-forecast/318251"
            ),
            dailyForecasts = listOf(
                DailyForecast(
                    date = now,
                    epochDate = (now.time / 1000).toInt(),
                    temperature = Temperature(
                        minimum = Minimum(
                            value = 14,
                            unit = "C",
                            unitType = 17
                        ),
                        maximum = Maximum(
                            value = 24,
                            unit = "C",
                            unitType = 17
                        )
                    ),
                    day = Day(
                        icon = 1,
                        iconPhrase = "Sunny",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    night = Night(
                        icon = 33,
                        iconPhrase = "Clear",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    sources = listOf("AccuWeather"),
                    mobileLink = "http://www.accuweather.com/en/au/sydney/2000/daily-weather-forecast/318251?day=1",
                    link = "http://www.accuweather.com/en/au/sydney/2000/daily-weather-forecast/318251?day=1"
                ),
                DailyForecast(
                    date = tomorrow,
                    epochDate = (tomorrow.time / 1000).toInt(),
                    temperature = Temperature(
                        minimum = Minimum(
                            value = 15,
                            unit = "C",
                            unitType = 17
                        ),
                        maximum = Maximum(
                            value = 25,
                            unit = "C",
                            unitType = 17
                        )
                    ),
                    day = Day(
                        icon = 2,
                        iconPhrase = "Mostly sunny",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    night = Night(
                        icon = 34,
                        iconPhrase = "Mostly clear",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    sources = listOf("AccuWeather"),
                    mobileLink = "http://www.accuweather.com/en/au/sydney/2000/daily-weather-forecast/318251?day=2",
                    link = "http://www.accuweather.com/en/au/sydney/2000/daily-weather-forecast/318251?day=2"
                ),
                DailyForecast(
                    date = dayAfterTomorrow,
                    epochDate = (dayAfterTomorrow.time / 1000).toInt(),
                    temperature = Temperature(
                        minimum = Minimum(
                            value = 16,
                            unit = "C",
                            unitType = 17
                        ),
                        maximum = Maximum(
                            value = 26,
                            unit = "C",
                            unitType = 17
                        )
                    ),
                    day = Day(
                        icon = 4,
                        iconPhrase = "Partly sunny",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    night = Night(
                        icon = 36,
                        iconPhrase = "Partly cloudy",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    sources = listOf("AccuWeather"),
                    mobileLink = "http://www.accuweather.com/en/au/sydney/2000/daily-weather-forecast/318251?day=3",
                    link = "http://www.accuweather.com/en/au/sydney/2000/daily-weather-forecast/318251?day=3"
                ),
                DailyForecast(
                    date = fourthDay,
                    epochDate = (fourthDay.time / 1000).toInt(),
                    temperature = Temperature(
                        minimum = Minimum(
                            value = 17,
                            unit = "C",
                            unitType = 17
                        ),
                        maximum = Maximum(
                            value = 27,
                            unit = "C",
                            unitType = 17
                        )
                    ),
                    day = Day(
                        icon = 3,
                        iconPhrase = "Partly sunny",
                        hasPrecipitation = true,
                        precipitationType = "Rain",
                        precipitationIntensity = "Light"
                    ),
                    night = Night(
                        icon = 35,
                        iconPhrase = "Partly cloudy",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    sources = listOf("AccuWeather"),
                    mobileLink = "http://www.accuweather.com/en/au/sydney/2000/daily-weather-forecast/318251?day=4",
                    link = "http://www.accuweather.com/en/au/sydney/2000/daily-weather-forecast/318251?day=4"
                ),
                DailyForecast(
                    date = fifthDay,
                    epochDate = (fifthDay.time / 1000).toInt(),
                    temperature = Temperature(
                        minimum = Minimum(
                            value = 18,
                            unit = "C",
                            unitType = 17
                        ),
                        maximum = Maximum(
                            value = 28,
                            unit = "C",
                            unitType = 17
                        )
                    ),
                    day = Day(
                        icon = 1,
                        iconPhrase = "Sunny",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    night = Night(
                        icon = 33,
                        iconPhrase = "Clear",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    sources = listOf("AccuWeather"),
                    mobileLink = "http://www.accuweather.com/en/au/sydney/2000/daily-weather-forecast/318251?day=5",
                    link = "http://www.accuweather.com/en/au/sydney/2000/daily-weather-forecast/318251?day=5"
                )
            )
        )
    }
    
    private fun createRomeForecast(): WeatherForecast {
        val now = Date()
        val tomorrow = Date(now.time + 24 * 60 * 60 * 1000)
        val dayAfterTomorrow = Date(now.time + 48 * 60 * 60 * 1000)
        val fourthDay = Date(now.time + 72 * 60 * 60 * 1000)
        val fifthDay = Date(now.time + 96 * 60 * 60 * 1000)

        return WeatherForecast(
            headline = Headline(
                effectiveDate = now,
                effectiveEpochDate = (now.time / 1000).toInt(),
                severity = 3,
                text = "Expect pleasant Mediterranean climate with mild temperatures",
                category = "sunny",
                endDate = fifthDay,
                endEpochDate = (fifthDay.time / 1000).toInt(),
                mobileLink = "http://www.accuweather.com/en/it/rome/316906/daily-weather-forecast/219373",
                link = "http://www.accuweather.com/en/it/rome/316906/daily-weather-forecast/219373"
            ),
            dailyForecasts = listOf(
                DailyForecast(
                    date = now,
                    epochDate = (now.time / 1000).toInt(),
                    temperature = Temperature(
                        minimum = Minimum(
                            value = 16,
                            unit = "C",
                            unitType = 17
                        ),
                        maximum = Maximum(
                            value = 25,
                            unit = "C",
                            unitType = 17
                        )
                    ),
                    day = Day(
                        icon = 1,
                        iconPhrase = "Sunny",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    night = Night(
                        icon = 33,
                        iconPhrase = "Clear",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    sources = listOf("AccuWeather"),
                    mobileLink = "http://www.accuweather.com/en/it/rome/316906/daily-weather-forecast/219373?day=1",
                    link = "http://www.accuweather.com/en/it/rome/316906/daily-weather-forecast/219373?day=1"
                ),
                DailyForecast(
                    date = tomorrow,
                    epochDate = (tomorrow.time / 1000).toInt(),
                    temperature = Temperature(
                        minimum = Minimum(
                            value = 17,
                            unit = "C",
                            unitType = 17
                        ),
                        maximum = Maximum(
                            value = 26,
                            unit = "C",
                            unitType = 17
                        )
                    ),
                    day = Day(
                        icon = 2,
                        iconPhrase = "Mostly sunny",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    night = Night(
                        icon = 34,
                        iconPhrase = "Mostly clear",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    sources = listOf("AccuWeather"),
                    mobileLink = "http://www.accuweather.com/en/it/rome/316906/daily-weather-forecast/219373?day=2",
                    link = "http://www.accuweather.com/en/it/rome/316906/daily-weather-forecast/219373?day=2"
                ),
                DailyForecast(
                    date = dayAfterTomorrow,
                    epochDate = (dayAfterTomorrow.time / 1000).toInt(),
                    temperature = Temperature(
                        minimum = Minimum(
                            value = 18,
                            unit = "C",
                            unitType = 17
                        ),
                        maximum = Maximum(
                            value = 27,
                            unit = "C",
                            unitType = 17
                        )
                    ),
                    day = Day(
                        icon = 4,
                        iconPhrase = "Partly sunny",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    night = Night(
                        icon = 36,
                        iconPhrase = "Partly cloudy",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    sources = listOf("AccuWeather"),
                    mobileLink = "http://www.accuweather.com/en/it/rome/316906/daily-weather-forecast/219373?day=3",
                    link = "http://www.accuweather.com/en/it/rome/316906/daily-weather-forecast/219373?day=3"
                ),
                DailyForecast(
                    date = fourthDay,
                    epochDate = (fourthDay.time / 1000).toInt(),
                    temperature = Temperature(
                        minimum = Minimum(
                            value = 17,
                            unit = "C",
                            unitType = 17
                        ),
                        maximum = Maximum(
                            value = 26,
                            unit = "C",
                            unitType = 17
                        )
                    ),
                    day = Day(
                        icon = 6,
                        iconPhrase = "Mostly cloudy",
                        hasPrecipitation = true,
                        precipitationType = "Rain",
                        precipitationIntensity = "Light"
                    ),
                    night = Night(
                        icon = 38,
                        iconPhrase = "Mostly cloudy",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    sources = listOf("AccuWeather"),
                    mobileLink = "http://www.accuweather.com/en/it/rome/316906/daily-weather-forecast/219373?day=4",
                    link = "http://www.accuweather.com/en/it/rome/316906/daily-weather-forecast/219373?day=4"
                ),
                DailyForecast(
                    date = fifthDay,
                    epochDate = (fifthDay.time / 1000).toInt(),
                    temperature = Temperature(
                        minimum = Minimum(
                            value = 19,
                            unit = "C",
                            unitType = 17
                        ),
                        maximum = Maximum(
                            value = 28,
                            unit = "C",
                            unitType = 17
                        )
                    ),
                    day = Day(
                        icon = 1,
                        iconPhrase = "Sunny",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    night = Night(
                        icon = 33,
                        iconPhrase = "Clear",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    sources = listOf("AccuWeather"),
                    mobileLink = "http://www.accuweather.com/en/it/rome/316906/daily-weather-forecast/219373?day=5",
                    link = "http://www.accuweather.com/en/it/rome/316906/daily-weather-forecast/219373?day=5"
                )
            )
        )
    }

    private fun createCopenhagenForecast(): WeatherForecast {
        val now = Date()
        val tomorrow = Date(now.time + 24 * 60 * 60 * 1000)
        val dayAfterTomorrow = Date(now.time + 48 * 60 * 60 * 1000)
        val fourthDay = Date(now.time + 72 * 60 * 60 * 1000)
        val fifthDay = Date(now.time + 96 * 60 * 60 * 1000)

        return WeatherForecast(
            headline = Headline(
                effectiveDate = now,
                effectiveEpochDate = (now.time / 1000).toInt(),
                severity = 3,
                text = "Expect variable conditions with occasional rain and moderate temperatures",
                category = "cloudy",
                endDate = fifthDay,
                endEpochDate = (fifthDay.time / 1000).toInt(),
                mobileLink = "http://www.accuweather.com/en/dk/copenhagen/261842/daily-weather-forecast/261842",
                link = "http://www.accuweather.com/en/dk/copenhagen/261842/daily-weather-forecast/261842"
            ),
            dailyForecasts = listOf(
                DailyForecast(
                    date = now,
                    epochDate = (now.time / 1000).toInt(),
                    temperature = Temperature(
                        minimum = Minimum(
                            value = 8,
                            unit = "C",
                            unitType = 17
                        ),
                        maximum = Maximum(
                            value = 14,
                            unit = "C",
                            unitType = 17
                        )
                    ),
                    day = Day(
                        icon = 6,
                        iconPhrase = "Mostly cloudy",
                        hasPrecipitation = true,
                        precipitationType = "Rain",
                        precipitationIntensity = "Light"
                    ),
                    night = Night(
                        icon = 38,
                        iconPhrase = "Mostly cloudy",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    sources = listOf("AccuWeather"),
                    mobileLink = "http://www.accuweather.com/en/dk/copenhagen/261842/daily-weather-forecast/261842?day=1",
                    link = "http://www.accuweather.com/en/dk/copenhagen/261842/daily-weather-forecast/261842?day=1"
                ),
                DailyForecast(
                    date = tomorrow,
                    epochDate = (tomorrow.time / 1000).toInt(),
                    temperature = Temperature(
                        minimum = Minimum(
                            value = 7,
                            unit = "C",
                            unitType = 17
                        ),
                        maximum = Maximum(
                            value = 13,
                            unit = "C",
                            unitType = 17
                        )
                    ),
                    day = Day(
                        icon = 7,
                        iconPhrase = "Cloudy",
                        hasPrecipitation = true,
                        precipitationType = "Rain",
                        precipitationIntensity = "Moderate"
                    ),
                    night = Night(
                        icon = 40,
                        iconPhrase = "Cloudy w/ showers",
                        hasPrecipitation = true,
                        precipitationType = "Rain",
                        precipitationIntensity = "Light"
                    ),
                    sources = listOf("AccuWeather"),
                    mobileLink = "http://www.accuweather.com/en/dk/copenhagen/261842/daily-weather-forecast/261842?day=2",
                    link = "http://www.accuweather.com/en/dk/copenhagen/261842/daily-weather-forecast/261842?day=2"
                ),
                DailyForecast(
                    date = dayAfterTomorrow,
                    epochDate = (dayAfterTomorrow.time / 1000).toInt(),
                    temperature = Temperature(
                        minimum = Minimum(
                            value = 9,
                            unit = "C",
                            unitType = 17
                        ),
                        maximum = Maximum(
                            value = 16,
                            unit = "C",
                            unitType = 17
                        )
                    ),
                    day = Day(
                        icon = 4,
                        iconPhrase = "Partly sunny",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    night = Night(
                        icon = 36,
                        iconPhrase = "Partly cloudy",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    sources = listOf("AccuWeather"),
                    mobileLink = "http://www.accuweather.com/en/dk/copenhagen/261842/daily-weather-forecast/261842?day=3",
                    link = "http://www.accuweather.com/en/dk/copenhagen/261842/daily-weather-forecast/261842?day=3"
                ),
                DailyForecast(
                    date = fourthDay,
                    epochDate = (fourthDay.time / 1000).toInt(),
                    temperature = Temperature(
                        minimum = Minimum(
                            value = 10,
                            unit = "C",
                            unitType = 17
                        ),
                        maximum = Maximum(
                            value = 17,
                            unit = "C",
                            unitType = 17
                        )
                    ),
                    day = Day(
                        icon = 2,
                        iconPhrase = "Mostly sunny",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    night = Night(
                        icon = 34,
                        iconPhrase = "Mostly clear",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    sources = listOf("AccuWeather"),
                    mobileLink = "http://www.accuweather.com/en/dk/copenhagen/261842/daily-weather-forecast/261842?day=4",
                    link = "http://www.accuweather.com/en/dk/copenhagen/261842/daily-weather-forecast/261842?day=4"
                ),
                DailyForecast(
                    date = fifthDay,
                    epochDate = (fifthDay.time / 1000).toInt(),
                    temperature = Temperature(
                        minimum = Minimum(
                            value = 11,
                            unit = "C",
                            unitType = 17
                        ),
                        maximum = Maximum(
                            value = 18,
                            unit = "C",
                            unitType = 17
                        )
                    ),
                    day = Day(
                        icon = 3,
                        iconPhrase = "Partly sunny",
                        hasPrecipitation = true,
                        precipitationType = "Rain",
                        precipitationIntensity = "Light"
                    ),
                    night = Night(
                        icon = 35,
                        iconPhrase = "Partly cloudy",
                        hasPrecipitation = false,
                        precipitationType = null,
                        precipitationIntensity = null
                    ),
                    sources = listOf("AccuWeather"),
                    mobileLink = "http://www.accuweather.com/en/dk/copenhagen/261842/daily-weather-forecast/261842?day=5",
                    link = "http://www.accuweather.com/en/dk/copenhagen/261842/daily-weather-forecast/261842?day=5"
                )
            )
        )
    }
}