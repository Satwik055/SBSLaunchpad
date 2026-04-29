package com.satwik.sbslaunchpad.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object ScreenWelcome : NavKey

@Serializable
data object ScreenLogin : NavKey

@Serializable
data object ScreenRegister : NavKey

@Serializable
data object ScreenCompleteProfile : NavKey

@Serializable
data object ScreenBlacklisted : NavKey

@Serializable
data object ScreenProfileInReview : NavKey

@Serializable
data object ScreenProfileRejected : NavKey

@Serializable
data object ScreenHome : NavKey

@Serializable
data object ScreenNotification : NavKey

@Serializable
data object ScreenSearch : NavKey

@Serializable
data object ScreenAccount : NavKey

@Serializable
data object ScreenEditProfile : NavKey

@Serializable
data class ScreenJobDetail(val id: String) : NavKey

@Serializable
data class ScreenInternshipDetail(val id: String) : NavKey
