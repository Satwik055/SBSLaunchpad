package com.satwik.sbslaunchpad.core.designsystem.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A cleaner way to apply custom shadows with adjustable opacity.
 */
fun Modifier.customShadow(
    elevation: Dp = 8.dp,
    shape: Shape,
    alpha: Float = 0.1f,
    shadowColor: Color = Color.Black
): Modifier = this.shadow(
    elevation = elevation,
    shape = shape,
    spotColor = shadowColor.copy(alpha = alpha),
    ambientColor = shadowColor.copy(alpha = alpha)
)
