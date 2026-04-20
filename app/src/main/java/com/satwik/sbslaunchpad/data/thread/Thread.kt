package com.satwik.sbslaunchpad.data.thread

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Thread(
    @SerialName("id")
    val id: Int,

    @SerialName("post_id")
    val postId: String,

    @SerialName("sender_id")
    val senderId: String,

    @SerialName("reciever_id")
    val recieverId: String,

    @SerialName("last_message")
    val lastMessage: String,

    @SerialName("last_message_time")
    val lastMessageTime: String,

    @SerialName("unread_count")
    val unreadCount: Int,
)