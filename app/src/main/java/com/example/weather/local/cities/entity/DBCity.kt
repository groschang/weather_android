package com.example.weather.local.cities.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weather.model.City
import com.example.weather.model.locationDescription


@Entity(tableName = "cities")
data class DBCity(
    @PrimaryKey(autoGenerate = true)
    var uId: Int = 0,
    val id: String,
    val name: String,
    val location: String
)

fun DBCity.toCity() = City(id, name)
fun City.toDBCity() = DBCity(id = id, name = name, location = locationDescription())