package com.satwik.sbslaunchpad.features.auth.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.satwik.sbslaunchpad.LocalHorizontalAppPadding
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadButton
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadTextFeild
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadTopAppBar
import com.satwik.sbslaunchpad.core.designsystem.components.TextFeildCard
import com.satwik.sbslaunchpad.core.designsystem.theme.SurfaceOutline

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    val emailState = rememberTextFieldState()
    val passwordState = rememberTextFieldState()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        LaunchpadTopAppBar(
            title = "Login",
            onBackClick = { /* TODO: Handle back */ }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = LocalHorizontalAppPadding.current)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            TextFeildCard {
                LaunchpadTextFeild(
                    state = emailState,
                    semantic = ContentType.EmailAddress,
                    placeholder = "Enter your email"
                )
                HorizontalDivider(thickness = 1.dp, color = SurfaceOutline)
                LaunchpadTextFeild(
                    state = passwordState,
                    semantic = ContentType.Password,
                    placeholder = "Enter your password",
                    isPassword = true
                )
            }


            Spacer(modifier = Modifier.weight(1f))

            LaunchpadButton(
                text = "Login",
                onClick = { /* TODO: Handle login */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 140.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginScreenPreview() {
    LoginScreen(modifier = Modifier.statusBarsPadding())
}
