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
    val createdAt: Instant? = null,
    @SerialName("req_cgpa")
    val reqCgpa: Double = 0.0,
    @SerialName("req_year")
    val reqYear: List<Int> = emptyList(),
    @SerialName("req_tenth_marks_percentage")
    val reqTenthMarksPercentage: Double = 0.0,
    @SerialName("req_twelfth_marks_percentage")
    val reqTwelfthMarksPercentage: Double = 0.0,
    @SerialName("req_courses")
    val reqCourses: List<String> = emptyList(),
    @SerialName("req_backlog")
    val reqBacklog: Int? = 0
)
