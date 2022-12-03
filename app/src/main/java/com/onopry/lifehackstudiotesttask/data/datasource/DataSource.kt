package com.onopry.lifehackstudiotesttask.data.datasource

import com.onopry.lifehackstudiotesttask.data.model.CompanyDetails
import com.onopry.lifehackstudiotesttask.data.model.CompanyItem
import com.onopry.lifehackstudiotesttask.data.utils.ApiResult
import com.onopry.lifehackstudiotesttask.data.utils.wrapRetrofitResponse
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    fun getCompanies(): Flow<ApiResult<List<CompanyItem>>>
    fun getDetails(id: Int): Flow<ApiResult<CompanyDetails>>
}

class CompaniesRemoteDataSource(private val api: CompaniesApi) : RemoteDataSource {
    override fun getCompanies() = wrapRetrofitResponse { api.fetchCompanies() }
    override fun getDetails(id: Int) = wrapRetrofitResponse { api.getCompanyDetails(id) }
}

