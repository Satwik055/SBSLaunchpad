package com.satwik.sbslaunchpad.data.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class ProfileUpdateRequest(
    @SerialName("id")
    val id: Int = 0,

    @SerialName("created_at")
    val createdAt: String = "",

    @SerialName("requested_by")
    val requestedBy: String = "",

    @SerialName("approved_by")
    val approvedBy: String? = null,

    @SerialName("note")
    val note: String = "",

    @SerialName("requested_changes")
    val requestedChanges: JsonObject,

    @SerialName("status")
    val status: RequestStatus = RequestStatus.IN_REVIEW,

    @SerialName("is_read")
    val isRead: Boolean = false
)