package com.example.weather.model

import com.google.gson.annotations.SerializedName


data class City(
    @SerializedName("Key") val id: String,
    @SerializedName("LocalizedName") val name: String,
    @SerializedName("GeoPosition") val position: Location? = null,
    @SerializedName("Country") val country: Country? = null,
    @SerializedName("AdministrativeArea") val area: Area? = null
)

fun City.locationDescription(): String {
    var description = ""
    area?.name.let { description += it }
    if (description.isNotEmpty()) description += ", "
    country?.name.let { description += it }
    return description
}