package com.satwik.sbslaunchpad.core.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import com.satwik.sbslaunchpad.features.account.AccountScreen
import com.satwik.sbslaunchpad.features.account.AccountViewModel
import com.satwik.sbslaunchpad.features.editprofile.EditProfileScreen
import com.satwik.sbslaunchpad.features.completeprofile.CompleteProfileScreen
import com.satwik.sbslaunchpad.features.login.LoginScreen
import com.satwik.sbslaunchpad.features.register.RegisterScreen
import com.satwik.sbslaunchpad.features.blacklist.BlacklistedAccountScreen
import com.satwik.sbslaunchpad.features.blacklist.BlacklistedAccountViewModel
import com.satwik.sbslaunchpad.features.profilereview.ProfileInReviewScreen
import com.satwik.sbslaunchpad.features.profilereview.ProfileInReviewViewModel
import com.satwik.sbslaunchpad.features.profilerejected.ProfileRejectedScreen
import com.satwik.sbslaunchpad.features.profilerejected.ProfileRejectedViewModel
import com.satwik.sbslaunchpad.features.detail.InternshipDetailsScreen
import com.satwik.sbslaunchpad.features.detail.JobDetailsScreen
import com.satwik.sbslaunchpad.features.home.HomeScreen
import com.satwik.sbslaunchpad.features.notifications.NotificationScreen
import com.satwik.sbslaunchpad.features.search.presentation.SearchScreen
import com.satwik.sbslaunchpad.features.welcome.WelcomeScreen

@Composable
fun NavigationRoot(modifier: Modifier = Modifier, backStack: MutableList<NavKey>) {
    val analytics: FirebaseAnalytics = koinInject()

    //Sending the screen views to firebase analytics
    LaunchedEffect(backStack.lastOrNull()) {
        val currentScreen = backStack.lastOrNull()
        if (currentScreen != null) {
            val screenName = currentScreen::class.simpleName ?: "Unknown"
            analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
                param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
                param(FirebaseAnalytics.Param.SCREEN_CLASS, screenName)
            }
        }
    }

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryProvider = { key ->
            when (key) {
                is ScreenWelcome -> {
                    NavEntry(key = key) {
                        WelcomeScreen(
                            onLoginClick = { backStack.add(ScreenLogin) },
                            onRegisterClick = { backStack.add(ScreenRegister) }
                        )
                    }
                }

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

                is ScreenProfileInReview -> {
                    NavEntry(key = key) {
                        val viewModel: ProfileInReviewViewModel = koinViewModel()
                        ProfileInReviewScreen(
                            onBackClick = { viewModel.logout() }
                        )
                    }
                }

                is ScreenProfileRejected -> {
                    NavEntry(key = key) {
                        val viewModel: ProfileRejectedViewModel = koinViewModel()
                        val uiState by viewModel.uiState.collectAsState()
                        ProfileRejectedScreen(
                            rejectionReason = "The picture you attached was blurry, please attatch a clear picture of yours",
                            uiState = uiState,
                            onBackClick = { viewModel.logout() },
                            onResubmitClick = { viewModel.resubmit() },
                            onClearError = { viewModel.clearError() }
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
                                backStack.add(ScreenWelcome)
                            },
                            onEditClick = {
                                backStack.add(ScreenEditProfile)
                            }
                        )
                    }
                }

                is ScreenEditProfile -> {
                    NavEntry(key = key) {
                        EditProfileScreen(
                            onBackClick = { backStack.remove(key) },
                            onSuccess = { backStack.remove(key) }
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
