package com.satwik.sbslaunchpad.features.register

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.satwik.sbslaunchpad.R
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadButton
import com.satwik.sbslaunchpad.core.designsystem.components.MultiTextField
import com.satwik.sbslaunchpad.core.designsystem.theme.TextPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.fontFamily

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onContinueClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Top Bar
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_carret2),
                    contentDescription = "Back",
                    modifier = Modifier.size(16.dp),
                    tint = TextPrimary
                )
            }

            Text(
                text = "Register",
                modifier = Modifier.align(Alignment.Center),
                style = TextStyle(
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                    color = TextPrimary
                )
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        val placeholders = remember {
            listOf(
                "Enter your email",
                "Enter your phone",
                "Enter your password",
                "Confirm your password"
            )
        }

        // Form
        MultiTextField(
            items = placeholders,
            values = listOf(email, phone, password, confirmPassword),
            onValueChange = { index, newValue ->
                when (index) {
                    0 -> email = newValue
                    1 -> phone = newValue
                    2 -> password = newValue
                    3 -> confirmPassword = newValue
                }
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        // Bottom Button
        LaunchpadButton(
            text = "Continue",
            onClick = onContinueClick,
            modifier = Modifier.padding(bottom = 32.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterScreenPreview() {
    RegisterScreen(modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp))
}
