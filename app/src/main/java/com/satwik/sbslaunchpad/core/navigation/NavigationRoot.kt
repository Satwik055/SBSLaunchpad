package com.satwik.sbslaunchpad.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import com.satwik.sbslaunchpad.features.account.presentation.AccountScreen
import com.satwik.sbslaunchpad.features.home.HomeScreen
import com.satwik.sbslaunchpad.features.notifications.NotificationScreen
import com.satwik.sbslaunchpad.features.search.presentation.SearchScreen

@Composable
fun NavigationRoot(modifier: Modifier = Modifier, backStack: MutableList<NavKey>) {
    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryProvider = { key ->
            when (key) {
                is ScreenHome -> {
                    NavEntry(key = key) {
                        HomeScreen(
                            onNotificationClick = { backStack.add(ScreenNotification) },
                            onSearchClick = { backStack.add(ScreenSearch) },
                            onProfileClick = { backStack.add(ScreenAccount) }
                        )
                    }
                }

                is ScreenNotification -> {
                    NavEntry(key = key) {
                        NotificationScreen(
                            onBackClick = { backStack.remove(key) }
                        )
                    }
                }

                is ScreenSearch -> {
                    NavEntry(key = key) {
                        SearchScreen()
                    }
                }

                is ScreenAccount -> {
                    NavEntry(key = key) {
                        AccountScreen(
                            onBackClick = { backStack.remove(key) }
                        )
                    }
                }

                else -> throw RuntimeException("Unknown key: $key")
            }
        }
    )
}