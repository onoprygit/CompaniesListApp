package com.onopry.lifehackstudiotesttask.app.di

import com.onopry.lifehackstudiotesttask.app.presentation.utils.addQueryParam
import com.onopry.lifehackstudiotesttask.data.datasource.CompaniesApi
import com.onopry.lifehackstudiotesttask.data.datasource.LocationApi
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpInterceptor() =
        HttpLoggingInterceptor().setLevel(BODY)

    @Provides
    @Singleton
    @RetrofitQualifiers.CompaniesInfo
    fun provideCompaniesOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

    @Provides
    @Singleton
    @RetrofitQualifiers.Location
    fun provideLocationOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
/*            .addQueryParam("format", "json")
            .addQueryParam("lang", "ru")*/
            .addQueryParam("apiKey", "c036f66917984746a4fc3ae138581bb9")
            .addInterceptor(interceptor)
            .build()

    @Provides
    @Singleton
    fun provideMoshi(): Moshi =
        Moshi.Builder().build()

    @Provides
    @Singleton
    @RetrofitQualifiers.CompaniesInfo
    fun provideCompaniesRetrofit(
        moshi: Moshi,
        @RetrofitQualifiers.CompaniesInfo client: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://lifehack.studio/test_task/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @Singleton
    @RetrofitQualifiers.Location
    fun provideLocationRetrofit(
        moshi: Moshi,
        @RetrofitQualifiers.Location client: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.geoapify.com/v1/geocode/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @Singleton
    fun provideCompanyApi(@RetrofitQualifiers.CompaniesInfo retrofit: Retrofit): CompaniesApi =
        retrofit.create(CompaniesApi::class.java)

    @Provides
    @Singleton
    fun provideLocationApi(@RetrofitQualifiers.Location retrofit: Retrofit): LocationApi =
        retrofit.create(LocationApi::class.java)
}