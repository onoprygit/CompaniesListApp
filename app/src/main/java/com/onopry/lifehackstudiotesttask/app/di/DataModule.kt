package com.onopry.lifehackstudiotesttask.app.di

import com.onopry.lifehackstudiotesttask.data.datasource.CompaniesApi
import com.onopry.lifehackstudiotesttask.data.datasource.CompaniesRemoteDataSource
import com.onopry.lifehackstudiotesttask.data.datasource.LocationApi
import com.onopry.lifehackstudiotesttask.data.datasource.LocationRemoteDataSource
import com.onopry.lifehackstudiotesttask.data.datasource.RemoteDataSource
import com.onopry.lifehackstudiotesttask.data.datasource.RemoteLocationSource
import com.onopry.lifehackstudiotesttask.data.repository.CompaniesRepository
import com.onopry.lifehackstudiotesttask.data.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideCompaniesDataSource(api: CompaniesApi): RemoteDataSource =
        CompaniesRemoteDataSource(api)

    @Provides
    @Singleton
    fun provideLocationDataSource(api: LocationApi): RemoteLocationSource =
        LocationRemoteDataSource(api)

    @Provides
    @Singleton
    fun provideRepository(
        companiesSource: RemoteDataSource,
        locationSource: RemoteLocationSource
    ): Repository =
        CompaniesRepository(companiesSource, locationSource)
}