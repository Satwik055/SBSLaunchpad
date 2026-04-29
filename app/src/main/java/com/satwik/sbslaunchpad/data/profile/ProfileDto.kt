package com.satwik.sbslaunchpad.data.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    val id: String,
    @SerialName("fcm_token")
    val fcmToken: String? = null,
    @SerialName("full_name")
    val fullName: String? = null,
    val email: String? = null,
    @SerialName("twelfth_marksheet_url")
    val twelfthMarksheetUrl: String? = null,
    @SerialName("tenth_marksheet_url")
    val tenthMarksheetUrl: String? = null,
    @SerialName("profile_image_url")
    val profileImageUrl: String? = null,
    val course: String? = null,
    val semester: Int? = null,
    val phone: String? = null,
    val backlogs: Int? = null,
    val cgpa: Double? = null,
    @SerialName("tenth_marks_percentage")
    val tenthMarksPercentage: Double? = null,
    @SerialName("twelfth_marks_percentage")
    val twelfthMarksPercentage: Double? = null,
    @SerialName("post_selected_in")
    val postSelectedIn: String? = null,
    val year: Int? = null,
    @SerialName("roll_number")
    val rollNumber: String? = null,
    @SerialName("permanent_address")
    val permanentAddress: String? = null,
    @SerialName("current_address")
    val currentAddress: String? = null,
    @SerialName("resume_url")
    val resumeUrl: String? = null,
    val status: ProfileStatus? = null,
    @SerialName("is_blacklisted")
    val isBlacklisted: Boolean? = null,
)



fun ProfileDto.toProfile() = Profile(
    id = id,
    fcmToken = fcmToken ?: "null",
    fullName = fullName ?: "null",
    email = email ?: "null",
    twelfthMarksheetUrl = twelfthMarksheetUrl ?: "null",
    tenthMarksheetUrl = tenthMarksheetUrl ?: "null",
    profileImageUrl = profileImageUrl ?: "null",
    course = course ?: "null",
    semester = semester ?: 0,
    phone = phone ?: "null",
    backlogs = backlogs ?: 0,
    cgpa = cgpa ?: 0.0,
    tenthMarksPercentage = tenthMarksPercentage ?: 0.0,
    twelfthMarksPercentage = twelfthMarksPercentage ?: 0.0,
    postSelectedIn = postSelectedIn ?: "null",
    year = year ?: 0,
    rollNumber = rollNumber ?: "null",
    permanentAddress = permanentAddress ?: "null",
    currentAddress = currentAddress ?: "null",
    resumeUrl = resumeUrl ?: "null",
    status = status ?: ProfileStatus.PROFILE_COMPLETION_REQUIRED,
    isBlacklisted = isBlacklisted ?: false,
)