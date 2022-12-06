package com.onopry.lifehackstudiotesttask.data.model

import com.squareup.moshi.JsonClass



@JsonClass(generateAdapter = true)
data class CompanyDetailsResponse(
    val description: String?,
    val id: String?,
    val img: String?,
    val lat: Double?,
    val lon: Double?,
    val name: String?,
    val phone: String?,
    val www: String?,
)

fun CompanyDetailsResponse.toDomain() = CompanyDetails(
    description = description,
    id = id,
    img = img,
    name = name,
    phone = phone,
    www = www,
    address = null,
    lat = lat,
    lon = lon
)

fun CompanyDetailsResponse.toDomain(address: String) = CompanyDetails(
    description = description,
    id = id,
    img = img,
    name = name,
    phone = phone,
    www = www,
    address = address,
    lat = lat,
    lon = lon
)

data class CompanyDetails(
    val description: String?,
    val id: String?,
    val img: String?,
    val name: String?,
    val phone: String?,
    val www: String?,
    val address: String?,
    val lat: Double?,
    val lon: Double?,
)