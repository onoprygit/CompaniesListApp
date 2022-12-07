package com.onopry.lifehackstudiotesttask.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationResponseResult(
    @Json(name = "address_line1")
    val address: String,
    @Json(name = "city")
    val city: String,
)

@JsonClass(generateAdapter = true)
data class LocationResponse(
    val results: List<LocationResponseResult>
)