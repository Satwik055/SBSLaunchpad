package com.satwik.sbslaunchpad.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object ScreenHome : NavKey

@Serializable
data object ScreenNotification : NavKey

@Serializable
data object ScreenSearch : NavKey

@Serializable
data object ScreenAccount : NavKey
