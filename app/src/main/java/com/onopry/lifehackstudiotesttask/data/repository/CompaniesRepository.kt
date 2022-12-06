package com.onopry.lifehackstudiotesttask.data.repository

import com.onopry.lifehackstudiotesttask.data.datasource.RemoteDataSource
import com.onopry.lifehackstudiotesttask.data.datasource.RemoteLocationSource
import com.onopry.lifehackstudiotesttask.data.model.CompanyDetails
import com.onopry.lifehackstudiotesttask.data.model.CompanyDetailsResponse
import com.onopry.lifehackstudiotesttask.data.model.CompanyItem
import com.onopry.lifehackstudiotesttask.data.model.toDomain
import com.onopry.lifehackstudiotesttask.data.utils.ApiError
import com.onopry.lifehackstudiotesttask.data.utils.ApiException
import com.onopry.lifehackstudiotesttask.data.utils.ApiResult
import com.onopry.lifehackstudiotesttask.data.utils.ApiSuccess

interface Repository {
    suspend fun getCompanies(): ApiResult<List<CompanyItem>>
    suspend fun getDetailsById(id: String): ApiResult<CompanyDetails>
}

class CompaniesRepository(
    private val remoteDataSource: RemoteDataSource,
    private val locationSource: RemoteLocationSource
) : Repository {
    override suspend fun getCompanies() = remoteDataSource.getCompanies()
    override suspend fun getDetailsById(id: String) =
        when (val res = remoteDataSource.getDetails(id)) {
            is ApiSuccess -> ApiSuccess(data = getCompanyWithLocation(res.data.first()))
            is ApiError -> ApiError(code = res.code, message = res.message)
            is ApiException -> ApiException(e = res.e)
        }

    private fun coordinatesIsNotBlank(lat: Double?, lon: Double?) =
        lat != null && lon != null && lat != 0.0 && lon != 0.0

    private suspend fun getCompanyWithLocation(company: CompanyDetailsResponse): CompanyDetails {
        val lat = company.lat
        val lon = company.lon

        if (coordinatesIsNotBlank(lat, lon)) {
            val location = locationSource.getLocation(lat!!, lon!!)

            if (location is ApiSuccess) {
                with(location.data.results.first()) {
                    return company.toDomain("г. $city, $address")
                }
            }
        }
        return company.toDomain()
    }
}

