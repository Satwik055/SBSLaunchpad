package com.satwik.sbslaunchpad.core.di

import com.satwik.sbslaunchpad.features.main.MainViewModel
import com.satwik.sbslaunchpad.features.notices.NoticeScreenViewModel
import com.satwik.sbslaunchpad.features.threads.ThreadViewModel
import com.satwik.sbslaunchpad.features.account.AccountViewModel
import com.satwik.sbslaunchpad.features.editprofile.EditProfileViewModel
import com.satwik.sbslaunchpad.features.login.LoginViewModel
import com.satwik.sbslaunchpad.features.register.RegisterViewModel
import com.satwik.sbslaunchpad.features.completeprofile.CompleteProfileViewModel
import com.satwik.sbslaunchpad.features.blacklist.BlacklistedAccountViewModel
import com.satwik.sbslaunchpad.features.profilereview.ProfileInReviewViewModel
import com.satwik.sbslaunchpad.features.profilerejected.ProfileRejectedViewModel
import com.satwik.sbslaunchpad.features.jobs.JobsViewModel
import com.satwik.sbslaunchpad.features.internships.InternshipsViewModel
import com.satwik.sbslaunchpad.features.detail.DetailViewModel
import com.satwik.sbslaunchpad.features.search.presentation.SearchViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::CompleteProfileViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::JobsViewModel)
    viewModelOf(::InternshipsViewModel)
    viewModelOf(::DetailViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::AccountViewModel)
    viewModelOf(::BlacklistedAccountViewModel)
    viewModelOf(::ProfileInReviewViewModel)
    viewModelOf(::ProfileRejectedViewModel)
    viewModelOf(::ThreadViewModel)
    viewModelOf(::NoticeScreenViewModel)
    viewModelOf(::EditProfileViewModel)
}