package com.satwik.sbslaunchpad.data.profile.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class RequestStatus{
    @SerialName("ACCEPTED")
    ACCEPTED,
    @SerialName("REJECTED")
    REJECTED,
    @SerialName("IN_REVIEW")
    IN_REVIEW,

}