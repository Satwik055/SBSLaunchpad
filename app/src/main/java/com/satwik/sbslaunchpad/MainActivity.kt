package com.satwik.sbslaunchpad

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.satwik.sbslaunchpad.core.designsystem.theme.BackgroundDefault
import com.satwik.sbslaunchpad.features.auth.completeprofile.CompleteProfileScreen
import com.satwik.sbslaunchpad.features.auth.login.LoginScreen
import com.satwik.sbslaunchpad.features.auth.register.RegisterScreen

val LocalHorizontalAppPadding = staticCompositionLocalOf { 16.dp }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainContent()
        }
    }
}

@Composable
fun MainContent() {
    CompositionLocalProvider(LocalHorizontalAppPadding provides 16.dp) {
        Scaffold(containerColor = BackgroundDefault) { innerPadding->
            CompleteProfileScreen(
                modifier = Modifier
                    .padding(innerPadding)
            )
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainContentPreview() {
    MainContent()
}
