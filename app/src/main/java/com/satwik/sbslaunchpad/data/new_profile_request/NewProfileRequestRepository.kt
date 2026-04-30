package com.satwik.sbslaunchpad.data.new_profile_request

import com.satwik.sbslaunchpad.data.new_profile_request.model.NewProfileRequest

interface NewProfileRequestRepository {
    suspend fun createNewProfileRequest(request: NewProfileRequest)
}