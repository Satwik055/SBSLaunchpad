package com.satwik.sbslaunchpad.core.di

import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.satwik.sbslaunchpad.core.util.ConnectivityManagerNetworkMonitor
import com.satwik.sbslaunchpad.core.util.Constants
import com.satwik.sbslaunchpad.core.util.NetworkMonitor
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.serializer.KotlinXSerializer
import io.github.jan.supabase.storage.Storage
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreModule = module {
    
    single<NetworkMonitor> { ConnectivityManagerNetworkMonitor(androidContext()) }
    single { CoroutineScope(Dispatchers.IO + SupervisorJob()) }
    single { FirebaseMessaging.getInstance() }
    single { FirebaseAnalytics.getInstance(androidContext()) }
    single {
        Firebase.remoteConfig.apply {
            val configSettings = remoteConfigSettings {
                minimumFetchIntervalInSeconds = 2
            }
            setConfigSettingsAsync(configSettings)
            setDefaultsAsync(mapOf("is_under_maintenance" to false))
        }
    }

    single {
        createSupabaseClient(
            supabaseUrl = Constants.SUPABASE_URL,
            supabaseKey = Constants.SUPABASE_KEY
        ) {
            httpEngine = OkHttp.create()
            install(Auth)
            install(Storage)
            install(Realtime)
            install(Postgrest) {
                serializer = KotlinXSerializer(Json {
                    explicitNulls = false
                    ignoreUnknownKeys = true 
                })
            }
        }
    }
}
