package com.satwik.sbslaunchpad.features.editprofile

import com.satwik.sbslaunchpad.data.profile.model.ProfileStatus

data class EditProfileFormState(
    val fullName: String = "",
    val fullNameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val course: String = "",
    val courseError: String? = null,
    val semester: String = "",
    val semesterError: String? = null,
    val phone: String = "",
    val phoneError: String? = null,
    val backlogs: String = "",
    val backlogsError: String? = null,
    val rollNumber: String = "",
    val rollNumberError: String? = null,
    val cgpa: String = "",
    val cgpaError: String? = null,
    val tenthMarksPercentage: String = "",
    val tenthMarksPercentageError: String? = null,
    val twelfthMarksPercentage: String = "",
    val twelfthMarksPercentageError: String? = null,
    val year: String = "",
    val yearError: String? = null,
    val permanentAddress: String = "",
    val permanentAddressError: String? = null,
    val currentAddress: String = "",
    val currentAddressError: String? = null,
    val profileImageUrl: String = "",
    val status: ProfileStatus = ProfileStatus.PROFILE_COMPLETION_REQUIRED,
    val rejectionReason: String? = null
)
