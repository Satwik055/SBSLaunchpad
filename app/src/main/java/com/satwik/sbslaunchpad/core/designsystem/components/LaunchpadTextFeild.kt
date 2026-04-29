package com.satwik.sbslaunchpad.core.designsystem.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.satwik.sbslaunchpad.core.designsystem.theme.SBSLaunchpadTheme
import com.satwik.sbslaunchpad.R
import com.satwik.sbslaunchpad.core.designsystem.theme.BrandPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.TextPlaceholder
import com.satwik.sbslaunchpad.core.designsystem.theme.fontFamily

@Composable
fun LaunchpadTextFeild(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    onValueChange: (String) -> Unit = {},
    semantic: ContentType = ContentType.Username,
    placeholder: String = "Enter your email",
    isPassword: Boolean = false,
    isError: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(state.text) {
        onValueChange(state.text.toString())
    }

    val textStyle = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
        letterSpacing = 0.1.sp

    )

    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        cursorColor = BrandPrimary,
        errorIndicatorColor = Color.Transparent,
        errorContainerColor = Color.Transparent
    )

    val trailingIconBlock: (@Composable () -> Unit)? = if (isPassword) {
        {
            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                Icon(
                    painter = painterResource(id = if (isPasswordVisible) R.drawable.ic_eye_show else R.drawable.ic_eye_hide),
                    contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                    tint = TextPlaceholder
                )
            }
        }
    } else null

    val placeholderBlock: @Composable () -> Unit = {
        Text(
            text = placeholder,
            style = textStyle.copy(color = TextPlaceholder),
        )
    }

    TextField(
        state = state,
        textStyle = textStyle,
        placeholder = placeholderBlock,
        trailingIcon = trailingIconBlock,
        lineLimits = TextFieldLineLimits.SingleLine,
        isError = isError,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        outputTransformation = if (isPassword && !isPasswordVisible) {
            OutputTransformation {
                // Obfuscate all characters with the dot symbol
                replace(0, length, "\u2022".repeat(length))
            }
        } else null,
        colors = textFieldColors,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .semantics { contentType = semantic }
    )
}

@Preview(showBackground = true)
@Composable
private fun LaunchpadTextFeildPreview() {
    SBSLaunchpadTheme {
        LaunchpadTextFeild(
            state = rememberTextFieldState(),
            semantic = ContentType.EmailAddress,
            placeholder = "Enter your email"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LaunchpadTextFeildPasswordPreview() {
    SBSLaunchpadTheme {
        LaunchpadTextFeild(
            state = rememberTextFieldState(),
            semantic = ContentType.Password,
            placeholder = "Enter your password",
            isPassword = true
        )
    }
}