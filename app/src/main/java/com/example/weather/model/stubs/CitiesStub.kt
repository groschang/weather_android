package com.example.weather.model.stubs

import com.example.weather.model.Area
import com.example.weather.model.City
import com.example.weather.model.Country
import com.example.weather.model.Location


object CitiesStub {

    val cities = listOf(
        City(
            id = "12345",
            name = "London",
            country = Country("United Kingdom"),
            area = Area("London"),
            position = Location(
                latitude = 51.514,
                longitude = -0.107
            )
        ),
        City(
            id = "67890",
            name = "Paris",
            country = Country("France"),
            area = Area("Île-de-France"),
            position = Location(
                latitude = 48.8566,
                longitude = 2.3522
            )
        ),
        City(
            id = "11121",
            name = "Tokyo",
            country = Country("Japan"),
            area = Area("Kantō"),
            position = Location(
                latitude = 35.6895, longitude = 139.6917
            )
        ),
        City(
            id = "17141",
            name = "New York",
            country = Country("United States"),
            area = Area("New York"),
            position = Location(
                latitude = 40.7128,
                longitude = -74.0059
            )
        ),
        City(
            id = "15161",
            name = "Sydney",
            country = Country("Australia"),
            area = Area("New South Wales"),
            position = Location(
                latitude = -33.8688,
                longitude = 151.2093
            )
        )
    )
}
