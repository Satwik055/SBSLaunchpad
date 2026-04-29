package com.satwik.sbslaunchpad.data.profile

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.selectAsFlow
import io.github.jan.supabase.realtime.selectSingleValueAsFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

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
                    client.postgrest.from("profile").selectSingleValueAsFlow(ProfileDto::id) {
                        eq("id", id)
                    }
                }
                else -> flowOf(null)
            }
        }
        .map { it?.toProfile() }
        .shareIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(7000),
            replay = 1
        )

    override suspend fun sendNewProfileRequest(request: NewProfileRequest): Result<Unit> {
        return try {
            client.postgrest.from("new_profile_request").insert(request)

            client.postgrest.from("profile").update(
                buildJsonObject {
                    put("status", ProfileStatus.IN_REVIEW.name)
                }
            ) {
                filter {
                    eq("id", request.requestedBy)
                }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendProfileUpdateRequest(request: ProfileUpdateRequest): Result<Unit> {
        return try {
            client.postgrest.from("profile_update_request").insert(request)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @OptIn(SupabaseExperimental::class)
    override fun getProfileUpdateRequest(): Flow<ProfileUpdateRequest?> {
        val id = client.auth.currentUserOrNull()?.id ?: return flowOf(null)
        return client.postgrest.from("profile_update_request").selectAsFlow(
            primaryKey = ProfileUpdateRequest::id,
            filter = FilterOperation("requested_by", FilterOperator.EQ, id)
        ).map { list ->
            list.filter { !it.isRead }.maxByOrNull { it.id }
        }
    }

    override suspend fun resubmitProfile(): Result<Unit> {
        return try {
            val userId = client.auth.currentUserOrNull()?.id ?: throw Exception("User not authenticated")
            client.postgrest.from("profile").update(
                buildJsonObject {
                    put("status", ProfileStatus.PROFILE_COMPLETION_REQUIRED.name)
                }
            ) {
                filter {
                    eq("id", userId)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun markProfileUpdateRequestAsRead(requestId: Int): Result<Unit> {
        return try {
            client.postgrest.from("profile_update_request").update(
                buildJsonObject {
                    put("is_read", true)
                }
            ) {
                filter {
                    eq("id", requestId)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
