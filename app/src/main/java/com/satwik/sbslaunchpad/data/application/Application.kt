package com.satwik.sbslaunchpad.data.application

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
@OptIn(ExperimentalTime::class)
data class Application(
    val id: Int? = null,
    @SerialName("student_id")
    val studentId: String,
    @SerialName("post_id")
    val postId: String,
    @SerialName("created_at")
    val createdAt: Instant? = null
)