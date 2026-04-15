package com.satwik.sbslaunchpad.core.designsystem.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.satwik.sbslaunchpad.R
import com.satwik.sbslaunchpad.core.designsystem.theme.DefaultElevation
import com.satwik.sbslaunchpad.core.designsystem.theme.ElevationStrength
import com.satwik.sbslaunchpad.core.designsystem.theme.SurfaceDefault
import com.satwik.sbslaunchpad.core.designsystem.theme.TextPlaceholder
import com.satwik.sbslaunchpad.core.designsystem.theme.fontFamily

// ─────────────────────────────────────────────────────────────────────────────
// SearchBarTrigger
// ─────────────────────────────────────────────────────────────────────────────
//
// Figma spec:
//   • Size        : 380 × 57 dp
//   • Background  : SurfaceDefault (white)
//   • Corner      : 12 dp
//   • Shadow      : 0 1 16  rgba(0,0,0,0.10)
//   • Leading icon: MagnifyingGlass 24 × 24 dp  (tint: TextPlaceholder #7C7C7C)
//   • Placeholder : "Search for jobs"  Poppins/Regular 14sp  #7C7C7C
//   • Padding     : start 16 dp, end 10 dp, vertical 10 dp
//
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarTrigger(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search for jobs",
) {
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        onClick = onClick,
        modifier = modifier
            .customShadow(
                elevation = DefaultElevation,
                shape = RoundedCornerShape(12.dp),
                alpha = ElevationStrength,
            )
            .fillMaxWidth()
            .height(57.dp),
        shape = RoundedCornerShape(12.dp),
        color = SurfaceDefault,
        interactionSource = interactionSource
    ) {
        TextFieldDefaults.DecorationBox(
            value = "",
            innerTextField = {},
            enabled = true,
            singleLine = true,
            visualTransformation = VisualTransformation.None,
            interactionSource = interactionSource,
            placeholder = {
                Text(
                    text = placeholder,
                    style = TextStyle(
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = TextPlaceholder,
                    ),
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = "Search",
                    tint = TextPlaceholder,
                    modifier = Modifier.size(20.dp),
                )
            },
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 10.dp,
                top = 10.dp,
                bottom = 10.dp,
            ),
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Preview
// ─────────────────────────────────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
private fun SearchBarTriggerPreview() {
    SearchBarTrigger(
        onClick = {},
        modifier = Modifier.padding(16.dp),
    )
}
