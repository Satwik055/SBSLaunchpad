package com.satwik.sbslaunchpad.features.detail.local_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.satwik.sbslaunchpad.core.designsystem.components.customShadow
import com.satwik.sbslaunchpad.core.designsystem.theme.DefaultElevation
import com.satwik.sbslaunchpad.core.designsystem.theme.ElevationStrength
import com.satwik.sbslaunchpad.core.designsystem.theme.SurfaceDefault
import com.satwik.sbslaunchpad.core.designsystem.theme.TextPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.TextSecondary
import com.satwik.sbslaunchpad.core.designsystem.theme.poppins

@Composable
fun JobDescriptionCard(
    modifier: Modifier = Modifier,
    description: String,
    compensation: String,
    requirements: String
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .customShadow(
                elevation = DefaultElevation,
                shape = RoundedCornerShape(20.dp),
                alpha = ElevationStrength
            )
            .clip(RoundedCornerShape(20.dp))
            .background(SurfaceDefault)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        DetailSection(
            title = "Description",
            content = description
        )

        DetailSection(
            title = "Compensation",
            content = compensation
        )

        DetailSection(
            title = "Requirements",
            content = requirements
        )
    }
}

@Composable
fun DetailSection(
    title: String,
    content: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontFamily = poppins,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                color = TextPrimary
            )
        )
        Text(
            text = content,
            style = TextStyle(
                fontFamily = poppins,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                color = TextSecondary,
                lineHeight = 20.sp
            )
        )
    }
}
