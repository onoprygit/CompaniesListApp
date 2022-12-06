package com.onopry.lifehackstudiotesttask.data.repository

import com.onopry.lifehackstudiotesttask.data.datasource.RemoteDataSource
import com.onopry.lifehackstudiotesttask.data.model.CompanyDetails
import com.onopry.lifehackstudiotesttask.data.model.CompanyItem
import com.onopry.lifehackstudiotesttask.data.utils.ApiError
import com.onopry.lifehackstudiotesttask.data.utils.ApiException
import com.onopry.lifehackstudiotesttask.data.utils.ApiResult
import com.onopry.lifehackstudiotesttask.data.utils.ApiSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface Repository {
    fun getCompanies(): Flow<ApiResult<List<CompanyItem>>>
    fun getDetailsById(id: String): Flow<ApiResult<CompanyDetails>>
}

class CompaniesRepository(private val remoteDataSource: RemoteDataSource) : Repository {
    override fun getCompanies() = remoteDataSource.getCompanies()
    override fun getDetailsById(id: String) = flow<ApiResult<CompanyDetails>> {
        val res = remoteDataSource.getDetails(id).collect {
            when (it) {
                is ApiSuccess -> emit(ApiSuccess(data = it.data.first()))
                is ApiError -> emit(ApiError(code = it.code, message = it.message))
                is ApiException -> emit(ApiException(e = it.e))
            }
        }
    }
}
