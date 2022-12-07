package com.onopry.lifehackstudiotesttask.data.datasource

import com.onopry.lifehackstudiotesttask.data.model.LocationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApi {
    @GET("./reverse?format=json&lang=ru")
    suspend fun getLocationByCoordinates(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Response<LocationResponse>
}