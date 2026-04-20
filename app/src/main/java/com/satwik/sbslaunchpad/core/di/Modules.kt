package com.satwik.sbslaunchpad.core.di

import com.satwik.sbslaunchpad.MainViewModel
import com.satwik.sbslaunchpad.core.fileUploader.CloudFileUploader
import com.satwik.sbslaunchpad.core.fileUploader.CloudFileUploaderSupabaseImpl
import com.satwik.sbslaunchpad.core.util.Constants
import com.satwik.sbslaunchpad.data.application_system.ApplicationSystem
import com.satwik.sbslaunchpad.data.application_system.ApplicationSystemSupabaseImpl
import com.satwik.sbslaunchpad.data.auth.AuthRepository
import com.satwik.sbslaunchpad.data.auth.SupabaseAuthRepositoryImpl
import com.satwik.sbslaunchpad.data.profile.ProfileRepository
import com.satwik.sbslaunchpad.data.profile.SupabaseProfileRepositoryImpl
import com.satwik.sbslaunchpad.data.post.PostRepository
import com.satwik.sbslaunchpad.data.post.SupabasePostRepositoryImpl
import com.satwik.sbslaunchpad.data.thread.ThreadRepository
import com.satwik.sbslaunchpad.data.thread.ThreadRepositorySupabaseImpl
import com.satwik.sbslaunchpad.data.admin.AdminRepository
import com.satwik.sbslaunchpad.data.admin.AdminRepositoryImpl
import com.satwik.sbslaunchpad.features.notifications.updates.ThreadViewModel
import com.satwik.sbslaunchpad.features.account.presentation.AccountViewModel
import com.satwik.sbslaunchpad.features.auth.AuthViewModel
import com.satwik.sbslaunchpad.features.auth.completeprofile.CompleteProfileViewModel
import com.satwik.sbslaunchpad.features.barriers.BlacklistedAccountViewModel
import com.satwik.sbslaunchpad.features.barriers.ProfileVerificationPendingViewModel
import com.satwik.sbslaunchpad.features.home.tabs.jobs.JobsViewModel
import com.satwik.sbslaunchpad.features.home.tabs.internships.InternshipsViewModel
import com.satwik.sbslaunchpad.features.detail.DetailViewModel
import com.satwik.sbslaunchpad.features.search.presentation.SearchViewModel
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.logging.LogLevel
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { CoroutineScope(Dispatchers.IO + SupervisorJob()) }

    single {
        createSupabaseClient(
            supabaseUrl = Constants.SUPABASE_URL,
            supabaseKey = Constants.SUPABASE_KEY
        ) {
            httpEngine = OkHttp.create()
            defaultLogLevel = LogLevel.DEBUG
            install(Auth)
            install(Storage)
            install(Postgrest)
            install(Realtime)
        }
    }

    single<PostRepository> { SupabasePostRepositoryImpl(get()) }
    single<ProfileRepository> { SupabaseProfileRepositoryImpl(get(), get()) }
    single<AuthRepository> { SupabaseAuthRepositoryImpl(get()) }
    single <CloudFileUploader>{ CloudFileUploaderSupabaseImpl(get()) }
    single<ApplicationSystem>{ ApplicationSystemSupabaseImpl(get()) }
    single<ThreadRepository> { ThreadRepositorySupabaseImpl(get()) }
    single<AdminRepository> { AdminRepositoryImpl(get()) }

    viewModelOf(::AuthViewModel)
    viewModelOf(::CompleteProfileViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::JobsViewModel)
    viewModelOf(::InternshipsViewModel)
    viewModelOf(::DetailViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::AccountViewModel)
    viewModelOf(::BlacklistedAccountViewModel)
    viewModelOf(::ProfileVerificationPendingViewModel)
    viewModelOf(::ThreadViewModel)
}
