package com.satwik.sbslaunchpad.features.editprofile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.input.rememberTextFieldState
import com.satwik.sbslaunchpad.core.designsystem.components.FieldError
import com.satwik.sbslaunchpad.core.designsystem.theme.BrandPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.TextPlaceholder
import com.satwik.sbslaunchpad.core.designsystem.theme.TextPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.TextSecondary
import com.satwik.sbslaunchpad.core.designsystem.theme.fontFamily
import androidx.compose.material3.HorizontalDivider
import com.satwik.sbslaunchpad.core.designsystem.theme.SurfaceOutline
import com.satwik.sbslaunchpad.core.designsystem.theme.SBSLaunchpadTheme

@Composable
fun EditProfileTextFeild(
    label: String,
    state: TextFieldState,
    placeholder: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    showDivider: Boolean = false
) {
    LaunchedEffect(state.text) {
        onValueChange(state.text.toString())
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)) {
            Text(
                text = label,
                style = TextStyle(
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = TextSecondary
                ),
            )
            Spacer(Modifier.height(10.dp))
            BasicTextField(
                state = state,
                enabled = enabled,
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = TextPrimary
                ),
                lineLimits = TextFieldLineLimits.SingleLine,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                cursorBrush = SolidColor(BrandPrimary),
                decorator = { innerTextField ->
                    Box {
                        if (state.text.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = TextStyle(
                                    fontFamily = fontFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp,
                                    color = TextPlaceholder
                                )
                            )
                        }
                        innerTextField()
                    }
                }
            )
            if (isError && errorMessage != null) {
                FieldError(text = errorMessage)
            }
        }

        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = SurfaceOutline
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditProfileTextFeildNormalPreview() {
    SBSLaunchpadTheme {
        EditProfileTextFeild(
            label = "Name",
            state = rememberTextFieldState("Satwik"),
            placeholder = "Enter name",
            onValueChange = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditProfileTextFeildErrorPreview() {
    SBSLaunchpadTheme {
        EditProfileTextFeild(
            label = "Email",
            state = rememberTextFieldState("satwik@"),
            placeholder = "Enter email",
            onValueChange = {},
            isError = true,
            errorMessage = "Please enter a valid email address"
        )
    }
}

