package com.satwik.sbslaunchpad.data.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
data class Profile(
    val id: String,
    @SerialName("full_name")
    val fullName: String,
    val email: String,
    @SerialName("twelfth_marksheet_url")
    val twelfthMarksheetUrl: String,
    @SerialName("tenth_marksheet_url")
    val tenthMarksheetUrl: String,
    @SerialName("profile_image_url")
    val profileImageUrl: String,
    val course: String = "",
    val semester: String = "",
    val phone: String = "",
    val backlogs: String = "",
    //--
    val cgpa: Double =  0.0,
    @SerialName("tenth_marks_percentage")
    val tenthMarksPercentage: Double = 0.0,
    @SerialName("twelfth_marks_percentage")
    val twelfthMarksPercentage: Double = 0.0,
    @SerialName("post_selected_in")
    val postSelectedIn:String? = "",
    val year:Int = 0,
    //--
    @SerialName("roll_number")
    val rollNumber: String = "",
    @SerialName("exam_roll_number")
    val examRollNumber: String = "",
    val address: String = "",
    @SerialName("resume_url")
    val resumeUrl: String,
    @SerialName("is_verified")
    val isVerified: Boolean = false,
    @SerialName("is_blacklisted")
    val isBlacklisted: Boolean = false,
    @SerialName("is_profile_completed")
    val isProfileCompleted: Boolean = false

)
