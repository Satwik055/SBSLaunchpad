package com.satwik.sbslaunchpad.core.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class LaunchpadApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@LaunchpadApp)
            modules(appModule)
        }
    }
}