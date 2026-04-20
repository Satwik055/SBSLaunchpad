package com.satwik.sbslaunchpad.core.navigation

import androidx.compose.runtime.Composable
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import com.satwik.sbslaunchpad.features.account.presentation.AccountScreen
import com.satwik.sbslaunchpad.features.account.presentation.AccountViewModel
import com.satwik.sbslaunchpad.features.completeprofile.CompleteProfileScreen
import com.satwik.sbslaunchpad.features.auth.login.LoginScreen
import com.satwik.sbslaunchpad.features.auth.register.RegisterScreen
import com.satwik.sbslaunchpad.features.blacklist.BlacklistedAccountScreen
import com.satwik.sbslaunchpad.features.blacklist.BlacklistedAccountViewModel
import com.satwik.sbslaunchpad.features.verification.ProfileVerificationPendingScreen
import com.satwik.sbslaunchpad.features.verification.ProfileVerificationPendingViewModel
import com.satwik.sbslaunchpad.features.detail.InternshipDetailsScreen
import com.satwik.sbslaunchpad.features.detail.JobDetailsScreen
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
                is ScreenLogin -> {
                    NavEntry(key = key) {
                        LoginScreen(
                            onLoginSuccess = {
                                backStack.clear()
                                backStack.add(ScreenHome)
                            },
                            onBackClick = {
                                if (backStack.size > 1) backStack.remove(key)
                            },
                            onSignUpClick = {
                                backStack.add(ScreenRegister)
                            }
                        )
                    }
                }

                is ScreenRegister -> {
                    NavEntry(key = key) {
                        RegisterScreen(
                            onContinueClick = {
                                backStack.add(ScreenCompleteProfile)
                            },
                            onBackClick = {
                                backStack.remove(key)
                            },
                            onLoginClick = {
                                backStack.remove(key)
                            }
                        )
                    }
                }

                is ScreenCompleteProfile -> {
                    NavEntry(key = key) {
                        CompleteProfileScreen()
                    }
                }

                is ScreenBlacklisted -> {
                    NavEntry(key = key) {
                        val viewModel: BlacklistedAccountViewModel = koinViewModel()
                        BlacklistedAccountScreen(
                            remark = "Testing Remark",
                            onBackClick = { viewModel.logout() }
                        )
                    }
                }

                is ScreenVerificationPending -> {
                    NavEntry(key = key) {
                        val viewModel: ProfileVerificationPendingViewModel = koinViewModel()
                        ProfileVerificationPendingScreen(
                            onBackClick = { viewModel.logout() }
                        )
                    }
                }

                is ScreenHome -> {
                    NavEntry(key = key) {
                        HomeScreen(
                            onNotificationClick = { backStack.add(ScreenNotification) },
                            onSearchClick = { backStack.add(ScreenSearch) },
                            onProfileClick = { backStack.add(ScreenAccount) },
                            onJobClick = { id -> backStack.add(ScreenJobDetail(id = id)) },
                            onInternshipClick = { id -> backStack.add(ScreenInternshipDetail(id = id)) }
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
                        SearchScreen(
                            onBackClick = { backStack.remove(key) },
                            onJobClick = { id -> backStack.add(ScreenJobDetail(id = id)) }
                        )
                    }
                }

                is ScreenAccount -> {
                    NavEntry(key = key) {
                        val viewModel: AccountViewModel = koinViewModel()
                        AccountScreen(
                            viewModel = viewModel,
                            onBackClick = { backStack.remove(key) },
                            onLogoutClick = {
                                backStack.clear()
                                backStack.add(ScreenLogin)
                            }
                        )
                    }
                }

                is ScreenJobDetail -> {
                    NavEntry(key = key) {
                        JobDetailsScreen(
                            id = key.id,
                            onBackClick = { backStack.remove(key) }
                        )
                    }
                }

                is ScreenInternshipDetail -> {
                    NavEntry(key = key) {
                        InternshipDetailsScreen(
                            id = key.id,
                            onBackClick = { backStack.remove(key) }
                        )
                    }
                }

                else -> throw RuntimeException("Unknown key: $key")
            }
        }
    )
}
