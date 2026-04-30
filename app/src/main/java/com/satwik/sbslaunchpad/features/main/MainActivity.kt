package com.satwik.sbslaunchpad.features.main

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation3.runtime.NavKey
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.satwik.sbslaunchpad.core.designsystem.components.NoInternetScreen
import com.satwik.sbslaunchpad.core.designsystem.theme.BrandPrimary
import com.satwik.sbslaunchpad.core.navigation.NavigationRoot
import com.satwik.sbslaunchpad.core.navigation.ScreenBlacklisted
import com.satwik.sbslaunchpad.core.navigation.ScreenCompleteProfile
import com.satwik.sbslaunchpad.core.navigation.ScreenHome
import com.satwik.sbslaunchpad.core.navigation.ScreenLogin
import com.satwik.sbslaunchpad.core.navigation.ScreenRegister
import com.satwik.sbslaunchpad.core.navigation.ScreenProfileInReview
import com.satwik.sbslaunchpad.core.navigation.ScreenProfileRejected
import com.satwik.sbslaunchpad.core.navigation.ScreenWelcome
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.compose.viewmodel.koinViewModel


val LocalHorizontalAppPadding = staticCompositionLocalOf { 16.dp }

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        setContent {
            MainContent()
        }
    }
}

@Composable
fun MainContent() {
    val viewModel: MainViewModel = koinViewModel()
    val appState by viewModel.appState.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()
    val isRetrying by viewModel.isRetrying.collectAsState()

    val requiredDestination = remember(appState) {
        when (appState) {
            is AppState.LoginRequired -> ScreenWelcome
            is AppState.Blacklisted -> ScreenBlacklisted
            is AppState.ProfileCompletionRequired -> ScreenCompleteProfile
            is AppState.ProfileInReview -> ScreenProfileInReview
            is AppState.ProfileRejected -> ScreenProfileRejected
            is AppState.Authorized -> ScreenHome
            else -> null
        }
    }

    var currentTargetDestination by remember { mutableStateOf<NavKey?>(requiredDestination) }
    LaunchedEffect(requiredDestination) {
        if (requiredDestination != null) {
            currentTargetDestination = requiredDestination
        }
    }

    val target = currentTargetDestination
    if (target == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = BrandPrimary)
        }
        return
    }

    val backStack = rememberNavBackStack(target)

    LaunchedEffect(target) {
        val currentDestination = backStack.lastOrNull()
        if (currentDestination != target) {
            val isCurrentDestinationAGate = currentDestination == ScreenWelcome ||
                    currentDestination == ScreenLogin ||
                    currentDestination == ScreenRegister ||
                    currentDestination == ScreenCompleteProfile ||
                    currentDestination == ScreenProfileInReview ||
                    currentDestination == ScreenProfileRejected ||
                    currentDestination == ScreenBlacklisted

            if (target != ScreenHome) {
                val isHandlingLoginGate = (target == ScreenWelcome || target == ScreenLogin) &&
                        (currentDestination == ScreenWelcome || currentDestination == ScreenLogin || currentDestination == ScreenRegister)

                if (!isHandlingLoginGate) {
                    backStack.clear()
                    backStack.add(target)
                }
            } else if (isCurrentDestinationAGate) {
                backStack.clear()
                backStack.add(ScreenHome)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        CompositionLocalProvider(LocalHorizontalAppPadding provides 16.dp) {
            if (isOnline) {
                NavigationRoot(
                    modifier = Modifier.statusBarsPadding(),
                    backStack = backStack
                )
            } else {
                NoInternetScreen(
                    modifier = Modifier.statusBarsPadding(),
                    onRetry = viewModel::retry,
                    isRetrying = isRetrying
                )
            }
        }

        if (appState is AppState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BrandPrimary)
            }
        }
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainContentPreview() {
    MainContent()
}
