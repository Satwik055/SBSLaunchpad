package com.satwik.sbslaunchpad.core.di

import com.google.firebase.messaging.FirebaseMessaging
import com.satwik.sbslaunchpad.core.util.Constants
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
import org.koin.dsl.module

val coreModule = module {
    single { CoroutineScope(Dispatchers.IO + SupervisorJob()) }
    single { FirebaseMessaging.getInstance() }
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