package com.onopry.lifehackstudiotesttask.data.repository

import com.onopry.lifehackstudiotesttask.data.datasource.RemoteDataSource
import com.onopry.lifehackstudiotesttask.data.model.CompanyDetails
import com.onopry.lifehackstudiotesttask.data.model.CompanyItem
import com.onopry.lifehackstudiotesttask.data.utils.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

interface Repository {
    fun getCompanies(): Flow<ApiResult<List<CompanyItem>>>
    fun getDetailsById(id: Int): Flow<ApiResult<CompanyDetails>>
}

class CompaniesRepository(private val remoteDataSource: RemoteDataSource): Repository {
    override fun getCompanies() = remoteDataSource.getCompanies()
    override fun getDetailsById(id: Int) = remoteDataSource.getDetails(id)
}