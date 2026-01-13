package com.conrad.weather.model.stubs

import com.conrad.weather.local.cities.entity.DBCity

object DBCityStub {

    val instance = DBCity(
        id = "123",
        name = "City",
        area = "Area",
        country = "Country"
    )
}
