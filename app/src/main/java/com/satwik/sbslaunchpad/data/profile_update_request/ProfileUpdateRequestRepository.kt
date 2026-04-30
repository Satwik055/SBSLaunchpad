package com.satwik.sbslaunchpad.data.profile_update_request

import com.satwik.sbslaunchpad.data.profile_update_request.model.ProfileUpdateRequest
import kotlinx.coroutines.flow.Flow

interface ProfileUpdateRequestRepository {
    suspend fun createProfileUpdateRequest(request: ProfileUpdateRequest)
    fun getProfileUpdateRequest(): Flow<ProfileUpdateRequest?>
    suspend fun updateProfileUpdateRequest(id: Int, request: ProfileUpdateRequest)
}