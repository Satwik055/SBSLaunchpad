package com.satwik.sbslaunchpad.data.post

data class Post(
    val id: String,
    val profile: String,
    val companyName: String,
    val deadline: String,
    val group: String,
    val city: String,
    val applicants: String,
    val amount: String, // stipend or salary
    val companyLogoUrl: String? = null,
    val type: PostType,
    val description: String = "",
    val requirements: String = "",
    val postedDate: String = ""
)
