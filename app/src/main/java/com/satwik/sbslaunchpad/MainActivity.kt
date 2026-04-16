package com.satwik.sbslaunchpad

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.satwik.sbslaunchpad.core.navigation.NavigationRoot
import com.satwik.sbslaunchpad.core.navigation.ScreenHome
import com.satwik.sbslaunchpad.core.navigation.ScreenLogin
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.compose.viewmodel.koinViewModel


val LocalHorizontalAppPadding = staticCompositionLocalOf { 16.dp }

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isInitializing.value
            }
        }
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
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val isInitializing by viewModel.isInitializing.collectAsState()

    if (isInitializing) {
        // You might want to show a splash screen or a loading indicator here
        return
    }

    val startDestination = if (isLoggedIn) ScreenHome else ScreenLogin
    val backStack = rememberNavBackStack(startDestination)
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
