package com.satwik.sbslaunchpad.core.designsystem.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.satwik.sbslaunchpad.core.designsystem.theme.DefaultElevation
import com.satwik.sbslaunchpad.core.designsystem.theme.ElevationStrength
import com.satwik.sbslaunchpad.core.designsystem.theme.SurfaceDefault

@Composable
fun TextFeildCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
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
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            content()
        }
    }
}