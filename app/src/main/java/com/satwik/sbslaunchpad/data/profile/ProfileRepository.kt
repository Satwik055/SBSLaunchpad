package com.satwik.sbslaunchpad.data.profile

import com.satwik.sbslaunchpad.data.profile.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    val profile: Flow<Profile?>
    suspend fun updateProfile(uuid:String, profile: Profile)
}
