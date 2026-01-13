package com.conrad.weather.local.cities.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.conrad.weather.model.Area
import com.conrad.weather.model.City
import com.conrad.weather.model.Country

@Entity(tableName = "cities")
data class DBCity(
    @PrimaryKey(autoGenerate = true)
    var uId: Int = 0,
    val id: String,
    val name: String,
    val area: String?,
    val country: String?
)

fun DBCity.toCity() = City(
    id = id,
    name = name,
    area = area?.let { Area(name = it) },
    country = country?.let { Country(name = country) }
)

fun City.toDBCity() = DBCity(
    id = id,
    name = name,
    area = area?.name,
    country = country?.name
)