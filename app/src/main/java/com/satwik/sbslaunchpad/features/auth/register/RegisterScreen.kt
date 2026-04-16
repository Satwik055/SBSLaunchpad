package com.satwik.sbslaunchpad.features.auth.register

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
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadTopAppBar
import com.satwik.sbslaunchpad.core.designsystem.theme.SurfaceOutline
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadTextFeild
import com.satwik.sbslaunchpad.core.designsystem.components.TextFeildCard

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onContinueClick: () -> Unit = {}
) {
    val emailState = rememberTextFieldState()
    val phoneState = rememberTextFieldState()
    val passwordState = rememberTextFieldState()
    val confirmPasswordState = rememberTextFieldState()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        LaunchpadTopAppBar(
            title = "Register",
            onBackClick = onBackClick
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
                    state = phoneState,
                    semantic = ContentType.PhoneNumber,
                    placeholder = "Enter your phone"
                )
                HorizontalDivider(thickness = 1.dp, color = SurfaceOutline)
                LaunchpadTextFeild(
                    state = passwordState,
                    semantic = ContentType.Password,
                    placeholder = "Enter your password",
                    isPassword = true
                )
                HorizontalDivider(thickness = 1.dp, color = SurfaceOutline)
                LaunchpadTextFeild(
                    state = confirmPasswordState,
                    semantic = ContentType.Password,
                    placeholder = "Confirm your password",
                    isPassword = true
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            LaunchpadButton(
                text = "Continue",
                onClick = onContinueClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 140.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RegisterScreenPreview() {
    RegisterScreen(modifier = Modifier.statusBarsPadding())
}
