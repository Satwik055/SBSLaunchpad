package com.satwik.sbslaunchpad.features.main

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.satwik.sbslaunchpad.R
import com.satwik.sbslaunchpad.core.designsystem.theme.BrandPrimary
import com.satwik.sbslaunchpad.core.navigation.NavigationRoot
import com.satwik.sbslaunchpad.core.navigation.ScreenBlacklisted
import com.satwik.sbslaunchpad.core.navigation.ScreenCompleteProfile
import com.satwik.sbslaunchpad.core.navigation.ScreenHome
import com.satwik.sbslaunchpad.core.navigation.ScreenLogin
import com.satwik.sbslaunchpad.core.navigation.ScreenRegister
import com.satwik.sbslaunchpad.core.navigation.ScreenVerificationPending
import com.satwik.sbslaunchpad.core.navigation.ScreenWelcome
import com.satwik.sbslaunchpad.core.pushNotification.PushNotificationService
import com.satwik.sbslaunchpad.features.welcome.WelcomeScreen
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

    if (appState is AppState.Loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = BrandPrimary)
        }
        return
    }

    val requiredDestination = remember(appState) {
        when (appState) {
            is AppState.LoginRequired -> ScreenWelcome
            is AppState.Blacklisted -> ScreenBlacklisted
            is AppState.ProfileCompletionRequired -> ScreenCompleteProfile
            is AppState.VerificationPending -> ScreenVerificationPending
            is AppState.Authorized -> ScreenHome
            else -> ScreenWelcome
        }
    }

    val backStack = rememberNavBackStack(requiredDestination)

    LaunchedEffect(requiredDestination) {
        val currentDestination = backStack.lastOrNull()
        if (currentDestination != requiredDestination) {
            val isCurrentDestinationAGate = currentDestination == ScreenWelcome ||
                    currentDestination == ScreenLogin ||
                    currentDestination == ScreenRegister ||
                    currentDestination == ScreenCompleteProfile ||
                    currentDestination == ScreenVerificationPending ||
                    currentDestination == ScreenBlacklisted

            if (requiredDestination != ScreenHome) {
                val isHandlingLoginGate = (requiredDestination == ScreenWelcome || requiredDestination == ScreenLogin) &&
                        (currentDestination == ScreenWelcome || currentDestination == ScreenLogin || currentDestination == ScreenRegister)

                if (!isHandlingLoginGate) {
                    backStack.clear()
                    backStack.add(requiredDestination)
                }
            } else if (isCurrentDestinationAGate) {
                backStack.clear()
                backStack.add(ScreenHome)
            }
        }
    }

    CompositionLocalProvider(LocalHorizontalAppPadding provides 16.dp) {
        NavigationRoot(
            modifier = Modifier.statusBarsPadding(),
            backStack = backStack
        )
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainContentPreview() {
    MainContent()
}
