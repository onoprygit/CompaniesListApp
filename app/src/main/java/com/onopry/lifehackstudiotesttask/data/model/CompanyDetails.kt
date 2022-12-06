package com.onopry.lifehackstudiotesttask.data.model

import com.squareup.moshi.JsonClass



@JsonClass(generateAdapter = true)
data class CompanyDetails(
    val description: String,
    val id: String?,
    val img: String?,
    val lat: Double?,
    val lon: Double?,
    val name: String?,
    val phone: String?,
    val www: String?,
)