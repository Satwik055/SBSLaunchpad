package com.satwik.sbslaunchpad.data.account

data class Account(
    val id: String,
    val name: String,
    val email: String,
    val profileImageUrl: String? = null,
    val bio: String = "",
    val university: String = "",
    val graduationYear: String = "",
    val course: String = "",
    val semester: String = "",
    val phone: String = "",
    val backlogs: String = "",
    val rollNumber: String = "",
    val resumeUrl: String? = null,
    val isVerified: Boolean = false,
    val isBlacklisted: Boolean = false
)
