package com.satwik.sbslaunchpad.data.profile_update_request

import com.satwik.sbslaunchpad.data.profile_update_request.model.ProfileUpdateRequest
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import timber.log.Timber

class SupabaseProfileUpdateRequestRepositoryImpl(
    private val client: SupabaseClient
) : ProfileUpdateRequestRepository {

    private val tag = "Timber-${this::class.simpleName}"

    override suspend fun createProfileUpdateRequest(request: ProfileUpdateRequest) {
        Timber.tag(tag).d("Sending profile update request: %s", request)
        try {
            client.postgrest.from("profile_update_request").insert(request)
            Timber.tag(tag).d("Successfully sent profile update request")
        } catch (e: Exception) {
            Timber.tag(tag).e(e, "Failed to send profile update request: %s", e.message)
            throw e
        }
    }

    @OptIn(SupabaseExperimental::class)
    override fun getProfileUpdateRequest(): Flow<ProfileUpdateRequest?> {
        val id = client.auth.currentUserOrNull()?.id ?: return flowOf(null).also {
            Timber.tag(tag).w("getProfileUpdateRequest: User not authenticated")
        }
        Timber.tag(tag).d("Fetching profile update requests for user: %s", id)
        return client.postgrest.from("profile_update_request").selectAsFlow(
            primaryKey = ProfileUpdateRequest::id,
            filter = FilterOperation("requested_by", FilterOperator.EQ, id)
        ).map { list ->
            list.filter { !it.isRead }.maxByOrNull { it.id }.also {
                Timber.tag(tag).d("Found latest unread profile update request: %s", it)
            }
        }.catch { e ->
            Timber.tag(tag).e(e, "Error fetching profile update requests for user: %s", id)
            throw e
        }
    }

    override suspend fun updateProfileUpdateRequest(id: Int, request: ProfileUpdateRequest) {
        Timber.tag(tag).d("Updating profile-update-request table | Id: ${request.id}...")
        try {
            client.postgrest.from("profile_update_request").update(request) {
                filter {
                    eq("id", id)
                }
            }
            Timber.tag(tag).d("Successfully updated profile-update-request table | Id: ${request.id}")
        } catch (e: Exception) {
            Timber.tag(tag).e(e, "Failed to profile-update-request table | Id: ${request.id} | Error : ${e.message}")
            throw e
        }
    }
}