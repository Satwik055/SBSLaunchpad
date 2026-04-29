package com.satwik.sbslaunchpad.data.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ProfileStatus {
    @SerialName("PROFILE_COMPLETION_REQUIRED")
    PROFILE_COMPLETION_REQUIRED,

    @SerialName("ACCEPTED")
    ACCEPTED,

    @SerialName("REJECTED")
    REJECTED,

    @SerialName("IN_REVIEW")
    IN_REVIEW
}