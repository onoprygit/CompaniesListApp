package com.onopry.lifehackstudiotesttask.data.datasource

import com.onopry.lifehackstudiotesttask.data.model.CompanyDetails
import com.onopry.lifehackstudiotesttask.data.model.CompanyItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CompaniesApi {
    @GET("test.php")
    suspend fun fetchCompanies(): Response<List<CompanyItem>>

    @GET("./test.php")
    suspend fun getCompanyDetails(@Query("id") id: Int): Response<CompanyDetails>
}