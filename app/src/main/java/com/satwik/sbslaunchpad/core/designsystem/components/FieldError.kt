package com.satwik.sbslaunchpad.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.satwik.sbslaunchpad.core.designsystem.theme.BackgroundDefault
import com.satwik.sbslaunchpad.core.designsystem.theme.DefaultElevation
import com.satwik.sbslaunchpad.core.designsystem.theme.ElevationStrength
import com.satwik.sbslaunchpad.core.designsystem.theme.Error
import com.satwik.sbslaunchpad.core.designsystem.theme.fontFamily

@Composable
fun FieldError(
    text: String,
    modifier: Modifier = Modifier,
    startPadding: Dp = 16.dp
) {
    val bubbleColor = Color(0xFFE3E3E3)

    Column(
        modifier = modifier
            .padding(start = startPadding, bottom = 8.dp, end = 16.dp)
            .width(IntrinsicSize.Max)
    ) {
        // Triangle
        Box(
            modifier = Modifier
                .padding(start = 12.dp)
                .size(12.dp)
                .drawBehind {
                    val path = Path().apply {
                        moveTo(size.width / 2f, 0f)
                        lineTo(size.width, size.height)
                        lineTo(0f, size.height)
                        close()
                    }
                    drawPath(path, color = bubbleColor)
                }

        )
        // Bubble body
        Box(
            modifier = Modifier
                .background(bubbleColor, RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = Error,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = text,
                    color = Color.Black,
                    style = TextStyle(
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 10.sp
                    )
                )
            }
        }
    }
}
