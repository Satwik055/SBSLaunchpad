package com.satwik.sbslaunchpad.core.di

import com.satwik.sbslaunchpad.MainViewModel
import com.satwik.sbslaunchpad.core.util.Constants
import com.satwik.sbslaunchpad.data.auth.AuthRepository
import com.satwik.sbslaunchpad.data.auth.SupabaseAuthRepositoryImpl
import com.satwik.sbslaunchpad.data.account.AccountRepository
import com.satwik.sbslaunchpad.data.account.AccountRepositoryDummyImpl
import com.satwik.sbslaunchpad.data.post.PostRepositoryDummyImpl
import com.satwik.sbslaunchpad.data.post.PostRepository
import com.satwik.sbslaunchpad.features.account.presentation.AccountViewModel
import com.satwik.sbslaunchpad.features.auth.AuthViewModel
import com.satwik.sbslaunchpad.features.home.HomeViewModel
import com.satwik.sbslaunchpad.features.detail.DetailViewModel
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single {
        createSupabaseClient(
            supabaseUrl = Constants.SUPABASE_URL,
            supabaseKey = Constants.SUPABASE_KEY
        ) {
            install(Auth)
        }
    }

    single<PostRepository> { PostRepositoryDummyImpl() }
    single<AccountRepository> { AccountRepositoryDummyImpl() }
    single<AuthRepository> { SupabaseAuthRepositoryImpl(get()) }

    viewModelOf(::MainViewModel)
    viewModelOf(::AuthViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::DetailViewModel)
    viewModelOf(::AccountViewModel)
}
