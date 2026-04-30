package com.satwik.sbslaunchpad.data.admin.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Admin(
    @SerialName("id")
    val id: String,

    @SerialName("created_at")
    val createdAt: String? = null,

    @SerialName("name")
    val name: String,

    @SerialName("position")
    val position: String
)