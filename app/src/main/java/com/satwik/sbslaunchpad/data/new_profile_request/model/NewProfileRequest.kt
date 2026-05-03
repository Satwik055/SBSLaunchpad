package com.satwik.sbslaunchpad.data.new_profile_request.model

import com.satwik.sbslaunchpad.data.profile.model.RequestStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewProfileRequest(

    // ── Request management fields ──────────────────────────────
    val id: Long? = null,

    @SerialName("created_at")
    val createdAt: String? = null,

    @SerialName("requested_by")
    val requestedBy: String,

    @SerialName("approved_by")
    val approvedBy: String? = null,

    val note: String = "",

    @SerialName("reviewed_at")
    val reviewedAt: String? = null,

    val status: RequestStatus = RequestStatus.IN_REVIEW,

    // ── Profile data fields ────────────────────────────────────
    @SerialName("full_name")
    val fullName: String = "",

    val email: String = "",

    @SerialName("profile_image_url")
    val profileImageUrl: String = "",

    val course: String = "",

    val semester: Int = 0,

    val year: Int = 0,

    @SerialName("roll_number")
    val rollNumber: String = "",

    @SerialName("permanent_address")
    val permanentAddress: String = "",

    @SerialName("current_address")
    val currentAddress: String = "",

    val backlogs: Int = 0,

    val cgpa: Float = 0f,

    @SerialName("tenth_marks_percentage")
    val tenthMarksPercentage: Float = 0f,

    @SerialName("twelfth_marks_percentage")
    val twelfthMarksPercentage: Float = 0f,

    @SerialName("resume_url")
    val resumeUrl: String = "",

    @SerialName("tenth_marksheet_url")
    val tenthMarksheetUrl: String = "",

    @SerialName("twelfth_marksheet_url")
    val twelfthMarksheetUrl: String = ""
)