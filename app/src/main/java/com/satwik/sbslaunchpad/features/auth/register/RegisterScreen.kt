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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.satwik.sbslaunchpad.LocalHorizontalAppPadding
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadButton
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadTopAppBar
import com.satwik.sbslaunchpad.core.designsystem.theme.SurfaceOutline
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadSnackbarHost
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadTextFeild
import com.satwik.sbslaunchpad.core.designsystem.components.TextFeildCard
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.satwik.sbslaunchpad.core.designsystem.theme.BackgroundDefault
import com.satwik.sbslaunchpad.core.designsystem.theme.BackgroundDefault
import com.satwik.sbslaunchpad.core.designsystem.theme.LaunchpadYellow
import com.satwik.sbslaunchpad.core.designsystem.theme.TextPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.fontFamily
import com.satwik.sbslaunchpad.features.auth.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = koinViewModel(),
    onBackClick: () -> Unit = {},
    onContinueClick: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    val emailState = rememberTextFieldState()
    val phoneState = rememberTextFieldState()
    val passwordState = rememberTextFieldState()
    val confirmPasswordState = rememberTextFieldState()
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.error) {
        if (uiState.error.isNotEmpty()) {
            snackbarHostState.showSnackbar(uiState.error)
        }
    }

    Scaffold(
        containerColor = BackgroundDefault,
        snackbarHost = { LaunchpadSnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
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
                    loading = uiState.isLoading,
                    onClick = {
                        viewModel.register(
                            email = emailState.text.toString(),
                            phone = phoneState.text.toString(),
                            password = passwordState.text.toString(),
                            onSuccess = onContinueClick
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Already have an account? ",
                        style = TextStyle(
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = "Login",
                        style = TextStyle(
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = LaunchpadYellow
                        ),
                        modifier = Modifier.clickable { onLoginClick() }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RegisterScreenPreview() {
    RegisterScreen(modifier = Modifier.statusBarsPadding())
}
