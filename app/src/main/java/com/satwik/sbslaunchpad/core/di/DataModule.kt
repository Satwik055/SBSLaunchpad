package com.satwik.sbslaunchpad.core.di

import com.satwik.sbslaunchpad.service.storage.CloudFileUploader
import com.satwik.sbslaunchpad.service.storage.SupabaseCloudFileUploaderImpl
import com.satwik.sbslaunchpad.data.application.ApplicationRepository
import com.satwik.sbslaunchpad.data.application.SupabaseApplicationRepositoryImpl
import com.satwik.sbslaunchpad.data.auth.AuthRepository
import com.satwik.sbslaunchpad.data.auth.SupabaseAuthRepositoryImpl
import com.satwik.sbslaunchpad.data.profile.ProfileRepository
import com.satwik.sbslaunchpad.data.profile.SupabaseProfileRepositoryImpl
import com.satwik.sbslaunchpad.data.profile_update_request.ProfileUpdateRequestRepository
import com.satwik.sbslaunchpad.data.profile_update_request.SupabaseProfileUpdateRequestRepositoryImpl
import com.satwik.sbslaunchpad.data.new_profile_request.NewProfileRequestRepository
import com.satwik.sbslaunchpad.data.new_profile_request.SupabaseNewProfileRequestRepositoryImpl
import com.satwik.sbslaunchpad.data.post.PostRepository
import com.satwik.sbslaunchpad.data.post.SupabasePostRepositoryImpl
import com.satwik.sbslaunchpad.data.thread.ThreadRepository
import com.satwik.sbslaunchpad.data.thread.SupabaseThreadRepositoryImpl
import com.satwik.sbslaunchpad.data.admin.AdminRepository
import com.satwik.sbslaunchpad.data.admin.AdminRepositoryImpl
import com.satwik.sbslaunchpad.data.notice.NoticeRepository
import com.satwik.sbslaunchpad.data.notice.SupabaseNoticeRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    singleOf(::SupabasePostRepositoryImpl) { bind<PostRepository>() }
    singleOf(::SupabaseProfileRepositoryImpl) { bind<ProfileRepository>() }
    singleOf(::SupabaseAuthRepositoryImpl) { bind<AuthRepository>() }
    singleOf(::SupabaseCloudFileUploaderImpl) { bind<CloudFileUploader>() }
    singleOf(::SupabaseApplicationRepositoryImpl) { bind<ApplicationRepository>() }
    singleOf(::SupabaseThreadRepositoryImpl) { bind<ThreadRepository>() }
    singleOf(::AdminRepositoryImpl) { bind<AdminRepository>() }
    singleOf(::SupabaseNoticeRepositoryImpl) { bind<NoticeRepository>() }
    singleOf(::SupabaseProfileUpdateRequestRepositoryImpl) { bind<ProfileUpdateRequestRepository>() }
    singleOf(::SupabaseNewProfileRequestRepositoryImpl) { bind<NewProfileRequestRepository>() }
}