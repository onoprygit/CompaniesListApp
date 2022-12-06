package com.onopry.lifehackstudiotesttask.core

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("365a0cde-33cf-48ef-88bc-a63ac1aa8cbb")
    }
}