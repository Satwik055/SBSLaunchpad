package com.satwik.sbslaunchpad

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.satwik.sbslaunchpad.core.designsystem.theme.SBSLaunchpadTheme
import com.satwik.sbslaunchpad.features.home.HomeScreen
import com.satwik.sbslaunchpad.features.login.LoginScreen
import com.satwik.sbslaunchpad.features.notifications.NotificationScreen
import com.satwik.sbslaunchpad.features.register.RegisterScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            )
        )
        setContent {
            MainContent()
        }
    }
}

@Composable
fun MainContent() {
    SBSLaunchpadTheme {
        HomeScreen(
            modifier = Modifier.statusBarsPadding()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainContentPreview() {
    MainContent()
}
