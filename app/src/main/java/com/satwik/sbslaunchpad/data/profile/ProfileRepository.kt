package com.satwik.sbslaunchpad.data.profile

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    val profile: Flow<Profile?>
    suspend fun updateProfile(profile: Profile): Result<Unit>
}
