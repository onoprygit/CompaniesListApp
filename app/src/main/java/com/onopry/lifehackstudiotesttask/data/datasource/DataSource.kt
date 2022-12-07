package com.onopry.lifehackstudiotesttask.data.datasource

import com.onopry.lifehackstudiotesttask.data.model.CompanyDetailsResponse
import com.onopry.lifehackstudiotesttask.data.model.CompanyItem
import com.onopry.lifehackstudiotesttask.data.model.LocationResponse
import com.onopry.lifehackstudiotesttask.data.utils.ApiResult
import com.onopry.lifehackstudiotesttask.data.utils.wrapRetrofitRequest

interface RemoteDataSource {
    suspend fun getCompanies(): ApiResult<List<CompanyItem>>
    suspend fun getDetails(id: String): ApiResult<List<CompanyDetailsResponse>>
}

interface RemoteLocationSource {
    suspend fun getLocation(lat: Double, lon: Double): ApiResult<LocationResponse>
}

class CompaniesRemoteDataSource(private val api: CompaniesApi) : RemoteDataSource {
    override suspend fun getCompanies() = wrapRetrofitRequest { api.fetchCompanies() }
    override suspend fun getDetails(id: String) = wrapRetrofitRequest { api.getCompanyDetails(id) }
}

class LocationRemoteDataSource(private val api: LocationApi) : RemoteLocationSource {
    override suspend fun getLocation(lat: Double, lon: Double) =
        wrapRetrofitRequest { api.getLocationByCoordinates(lat, lon) }
}

