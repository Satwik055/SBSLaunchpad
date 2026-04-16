package com.satwik.sbslaunchpad

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.rememberNavBackStack
import com.satwik.sbslaunchpad.core.navigation.NavigationRoot
import com.satwik.sbslaunchpad.core.navigation.ScreenHome


val LocalHorizontalAppPadding = staticCompositionLocalOf { 16.dp }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
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
    val backStack = rememberNavBackStack(ScreenHome)
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
