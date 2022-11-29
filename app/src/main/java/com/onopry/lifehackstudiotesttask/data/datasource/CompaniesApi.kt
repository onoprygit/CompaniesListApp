package com.onopry.lifehackstudiotesttask.data.datasource

import com.onopry.lifehackstudiotesttask.domain.model.CompanyItem
import retrofit2.Response
import retrofit2.http.GET

interface CompaniesApi {
    @GET("./test.php")
    fun fetchCompanies(): Response<List<CompanyItem>>
}