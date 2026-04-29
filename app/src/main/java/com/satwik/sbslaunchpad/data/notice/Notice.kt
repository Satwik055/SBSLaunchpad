package com.satwik.sbslaunchpad.data.notice

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Notice(
    val id: Int,

    @SerialName("title")
    val title: String,

    val body: String,

    @SerialName("sender_id")
    val senderId: String,

    @SerialName("receiver_id")
    val receiverId: String? = null,

    @SerialName("created_at")
    val createdAt: String? = null,

    @SerialName("is_read")
    val isRead: Boolean = false
)
