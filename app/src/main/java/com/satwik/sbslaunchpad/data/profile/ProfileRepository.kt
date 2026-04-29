package com.satwik.sbslaunchpad.data.profile

import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    val profile: Flow<Profile?>
    suspend fun sendNewProfileRequest(request: NewProfileRequest): Result<Unit>
    suspend fun sendProfileUpdateRequest(request: ProfileUpdateRequest): Result<Unit>

    fun getProfileUpdateRequest(): Flow<ProfileUpdateRequest?>
    suspend fun resubmitProfile(): Result<Unit>
    suspend fun markProfileUpdateRequestAsRead(requestId: Int): Result<Unit>
}
