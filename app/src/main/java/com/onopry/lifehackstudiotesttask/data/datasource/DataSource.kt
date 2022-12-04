package com.onopry.lifehackstudiotesttask.data.datasource

import com.onopry.lifehackstudiotesttask.data.model.CompanyDetails
import com.onopry.lifehackstudiotesttask.data.model.CompanyItem
import com.onopry.lifehackstudiotesttask.data.utils.ApiError
import com.onopry.lifehackstudiotesttask.data.utils.ApiException
import com.onopry.lifehackstudiotesttask.data.utils.ApiResult
import com.onopry.lifehackstudiotesttask.data.utils.ApiSuccess
import com.onopry.lifehackstudiotesttask.data.utils.wrapRetrofitResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

interface RemoteDataSource {
    fun getCompanies(): Flow<ApiResult<List<CompanyItem>>>
    fun getDetails(id: String): Flow<ApiResult<CompanyDetails>>
}

class CompaniesRemoteDataSource(private val api: CompaniesApi) : RemoteDataSource {
    override fun getCompanies() = wrapRetrofitResponse { api.fetchCompanies() }
    override fun getDetails(id: String) = wrapRetrofitResponse { api.getCompanyDetails(id) }
}

