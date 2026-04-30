package com.satwik.sbslaunchpad.data.profile

import com.satwik.sbslaunchpad.data.profile.model.Profile
import com.satwik.sbslaunchpad.data.profile.model.ProfileDto
import com.satwik.sbslaunchpad.data.profile.model.toDto
import com.satwik.sbslaunchpad.data.profile.model.toProfile
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.selectSingleValueAsFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import timber.log.Timber

class SupabaseProfileRepositoryImpl(
    private val client: SupabaseClient,
    private val scope: CoroutineScope
) : ProfileRepository {

    private val tag = "Timber-${this::class.simpleName}"


    @OptIn(SupabaseExperimental::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    override val profile: Flow<Profile?> = client.auth.sessionStatus
        .flatMapLatest { status ->
            Timber.tag(tag).d("Profile session status: %s", status)
            when (status) {
                is SessionStatus.Authenticated -> {
                    val id = status.session.user?.id ?: return@flatMapLatest flowOf(null).also {
                        Timber.tag(tag).w("Authenticated but user ID is null")
                    }
                    client.postgrest.from("profile").selectSingleValueAsFlow(ProfileDto::id) {
                        eq("id", id)
                    }
                }
                else -> flowOf(null)
            }
        }
        .map { it?.toProfile() }
        .catch { e ->
            Timber.tag(tag).e(e, "Error in profile flow: %s", e.message)
            throw e
        }
        .shareIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(7000),
            replay = 1
        )

    override suspend fun updateProfile(uuid:String, profile: Profile) {
        Timber.tag(tag).d("Updating profile for user: %s", profile.id)
        try {
            client.postgrest.from("profile").update(profile.toDto()) {
                filter {
                    eq("id", uuid)
                }
            }
            Timber.tag(tag).d("Successfully updated profile for user: %s", profile.id)
        } catch (e: Exception) {
            Timber.tag(tag).e(e, "Failed to update profile for user: %s, error: %s", profile.id, e.message)
            throw e
        }
    }
}
