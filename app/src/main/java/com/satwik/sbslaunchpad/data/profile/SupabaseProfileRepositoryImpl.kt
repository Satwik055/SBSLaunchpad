package com.satwik.sbslaunchpad.data.profile

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.selectSingleValueAsFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn

class SupabaseProfileRepositoryImpl(
    private val client: SupabaseClient,
    private val scope: CoroutineScope
) : ProfileRepository {

    @OptIn(SupabaseExperimental::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    override val profile: Flow<Profile?> = client.auth.sessionStatus
        .flatMapLatest { status ->
            when (status) {
                is SessionStatus.Authenticated -> {
                    val id = status.session.user?.id ?: return@flatMapLatest flowOf(null)
                    client.postgrest.from("profile").selectSingleValueAsFlow(Profile::id) {
                        eq("id", id)
                    }
                }
                else -> flowOf(null)
            }
        }
        .shareIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(7000),
            replay = 1
        )

    override suspend fun updateProfile(profile: Profile): Result<Unit> {
        return try {
            client.postgrest.from("profile").upsert(profile) {
                onConflict = "id"
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
