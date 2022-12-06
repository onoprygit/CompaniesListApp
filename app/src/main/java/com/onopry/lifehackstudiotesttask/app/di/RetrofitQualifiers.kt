package com.onopry.lifehackstudiotesttask.app.di

import javax.inject.Qualifier

object RetrofitQualifiers {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Location

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CompaniesInfo
}