package com.satwik.sbslaunchpad.data.thread

import com.satwik.sbslaunchpad.data.admin.Admin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Message(
    @SerialName("id")
    val id: Int,

    @SerialName("thread_id")
    val threadId: Int,

    @SerialName("sender_id")
    val senderId: String,

    @SerialName("content")
    val content: String = "",

    @SerialName("is_read")
    val isRead: Boolean = false,

    val admin: Admin? = null,

    @SerialName("created_at")
    val createdAt: String? = null,
)