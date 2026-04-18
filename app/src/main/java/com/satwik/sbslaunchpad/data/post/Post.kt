@file:OptIn(kotlin.time.ExperimentalTime::class)
package com.satwik.sbslaunchpad.data.post

import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: String,
    @SerialName("job_profile")
    val jobProfile: String,
    @SerialName("company_name")
    val companyName: String,
    val deadline: Instant,
    val group: String,
    val city: String,
    val applicants: String,
    val amount: Int,
    @SerialName("company_logo_url")
    val companyLogoUrl: String? = null,
    val type: PostType,
    val description: String = "",
    val requirements: String = "",
    @SerialName("posted_by")
    val postedBy: String = "",
    val note: String = "",
    @SerialName("selection_process")
    val selectionProcess: String = "",
    @SerialName("created_at")
    val createdAt: Instant? = null
)
