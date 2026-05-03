package com.satwik

import android.app.Application
import com.satwik.sbslaunchpad.BuildConfig
import com.satwik.sbslaunchpad.core.di.coreModule
import com.satwik.sbslaunchpad.core.di.dataModule
import com.satwik.sbslaunchpad.core.di.viewModelModule
import com.satwik.sbslaunchpad.core.logging.CrashReportingForRelease
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class LaunchpadApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@LaunchpadApp)
            modules(
                coreModule,
                dataModule,
                viewModelModule
            )
        }
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree()) // Logs everything in debug
        } else {
            Timber.plant(CrashReportingForRelease()) // Custom tree for release
        }
    }
}