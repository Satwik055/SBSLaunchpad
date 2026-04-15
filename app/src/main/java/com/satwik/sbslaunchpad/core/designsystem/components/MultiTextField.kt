package com.satwik.sbslaunchpad.core.designsystem.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.satwik.sbslaunchpad.core.designsystem.theme.BrandPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.DefaultElevation
import com.satwik.sbslaunchpad.core.designsystem.theme.ElevationStrength
import com.satwik.sbslaunchpad.core.designsystem.theme.SurfaceDefault
import com.satwik.sbslaunchpad.core.designsystem.theme.SurfaceOutline
import com.satwik.sbslaunchpad.core.designsystem.theme.TextPlaceholder
import com.satwik.sbslaunchpad.core.designsystem.theme.fontFamily

@Composable
fun MultiTextField(
    modifier: Modifier = Modifier,
    items: List<String>,
    values: List<String>,
    fontSize: TextUnit = 14.sp,
    onValueChange: (Int, String) -> Unit
) {
    // Optimization: Remember styles and colors to avoid recreation on every keystroke
    val textStyle = remember(fontSize) {
        TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = fontSize,
            color = Color.Black
        )
    }

    val placeholderStyle = remember(fontSize) {
        textStyle.copy(color = TextPlaceholder)
    }

    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        cursorColor = BrandPrimary
    )

    Surface(
        modifier = modifier
            .customShadow(
                elevation = DefaultElevation,
                shape = RoundedCornerShape(12.dp),
                alpha = ElevationStrength,
            )
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = SurfaceDefault
    ) {
        Column(
            modifier = Modifier.padding(vertical = 5.dp)
        ) {
            items.forEachIndexed { index, placeholder ->
                // Optimization: Use key to help Compose identify items efficiently
                key(index) {
                    TextField(
                        value = values.getOrElse(index) { "" },
                        onValueChange = { onValueChange(index, it) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                text = placeholder,
                                style = placeholderStyle
                            )
                        },
                        textStyle = textStyle,
                        colors = textFieldColors,
                        singleLine = true
                    )

                    if (index < items.size - 1) {
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = SurfaceOutline
                        )
                    }
                }
            }
        }
    }
}
