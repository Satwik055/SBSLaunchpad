package com.satwik.sbslaunchpad.data.new_profile_request

import com.satwik.sbslaunchpad.data.new_profile_request.model.NewProfileRequest
import com.satwik.sbslaunchpad.data.profile.model.ProfileStatus
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import timber.log.Timber

class SupabaseNewProfileRequestRepositoryImpl(
    private val client: SupabaseClient
) : NewProfileRequestRepository {

    private val tag = "Timber-${this::class.simpleName}"

    override suspend fun createNewProfileRequest(request: NewProfileRequest) {
        Timber.tag(tag).d("Sending new profile request for user: %s", request.requestedBy)
        try {
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
            Timber.tag(tag).d("Successfully sent new profile request and updated status to IN_REVIEW")
        } catch (e: Exception) {
            Timber.tag(tag).e(e, "Failed to send new profile request: %s", e.message)
            throw e
        }
    }
}