package com.satwik.sbslaunchpad.features.login

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.satwik.sbslaunchpad.features.main.LocalHorizontalAppPadding
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadButton
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadTextFeild
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadTopAppBar
import com.satwik.sbslaunchpad.core.designsystem.components.FieldError
import com.satwik.sbslaunchpad.core.designsystem.components.TextFeildCard
import com.satwik.sbslaunchpad.core.designsystem.theme.SurfaceOutline
import org.koin.compose.viewmodel.koinViewModel

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadSnackbarHost
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.satwik.sbslaunchpad.core.designsystem.theme.BackgroundDefault
import com.satwik.sbslaunchpad.core.designsystem.theme.LaunchpadYellow
import com.satwik.sbslaunchpad.core.designsystem.theme.TextPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.fontFamily

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = koinViewModel(),
    onBackClick: () -> Unit = {},
    onLoginSuccess: () -> Unit = {},
    onSignUpClick: () -> Unit = {}
) {
    val emailState = rememberTextFieldState()
    val passwordState = rememberTextFieldState()
    
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.validationEvents) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                LoginViewModel.ValidationEvent.Success -> {
                    viewModel.login(onSuccess = onLoginSuccess)
                }
            }
        }
    }

    LaunchedEffect(uiState.error) {
        if (uiState.error.isNotEmpty()) {
            snackbarHostState.showSnackbar(uiState.error)
            viewModel.clearError()
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
                title = "Login",
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
                    Column {
                        LaunchpadTextFeild(
                            state = emailState,
                            onValueChange = { viewModel.onEvent(LoginFormEvent.EmailChanged(it)) },
                            semantic = ContentType.EmailAddress,
                            placeholder = "Enter your email",
                            isError = formState.emailError != null
                        )
                        if (formState.emailError != null) {
                            FieldError(text = formState.emailError!!)
                        }
                    }
                    HorizontalDivider(thickness = 1.dp, color = SurfaceOutline)
                    Column {
                        LaunchpadTextFeild(
                            state = passwordState,
                            onValueChange = { viewModel.onEvent(LoginFormEvent.PasswordChanged(it)) },
                            semantic = ContentType.Password,
                            placeholder = "Enter your password",
                            isPassword = true,
                            isError = formState.passwordError != null
                        )
                        if (formState.passwordError != null) {
                            FieldError(text = formState.passwordError!!)
                        }
                    }
                }


                Spacer(modifier = Modifier.weight(1f))

                LaunchpadButton(
                    text = "Login",
                    loading = uiState.isLoading,
                    onClick = {
                        viewModel.onEvent(LoginFormEvent.Submit)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                )
                Spacer(Modifier.height(40.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Don't have an account? ",
                        style = TextStyle(
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = "Sign Up",
                        style = TextStyle(
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = LaunchpadYellow
                        ),
                        modifier = Modifier.clickable { onSignUpClick() }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginScreenPreview() {
    LoginScreen(modifier = Modifier.statusBarsPadding())
}
