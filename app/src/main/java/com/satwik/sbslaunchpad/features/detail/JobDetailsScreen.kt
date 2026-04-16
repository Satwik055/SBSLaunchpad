package com.satwik.sbslaunchpad.features.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.satwik.sbslaunchpad.R
import com.satwik.sbslaunchpad.core.designsystem.components.InfoChip
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadButton
import com.satwik.sbslaunchpad.core.designsystem.theme.BackgroundDefault
import com.satwik.sbslaunchpad.core.designsystem.theme.OnNeutralContainer
import com.satwik.sbslaunchpad.core.designsystem.theme.SBSLaunchpadTheme
import com.satwik.sbslaunchpad.core.designsystem.theme.TextSecondary
import com.satwik.sbslaunchpad.core.designsystem.theme.poppins
import com.satwik.sbslaunchpad.features.auth.local_component.ScrollShadows
import com.satwik.sbslaunchpad.features.detail.local_components.JobDescriptionCard
import com.satwik.sbslaunchpad.features.detail.local_components.JobDetailsTopBar
import com.satwik.sbslaunchpad.features.detail.local_components.NoteSection

@Composable
fun JobDetailsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onApplyClick: () -> Unit = {},
    companyLogoUrl: String? = null
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDefault)
    ) {
        JobDetailsTopBar(
            title = "Audit Assistant",
            company = "Deloitte",
            onBackClick = onBackClick,
            companyLogoUrl = companyLogoUrl
        )

        ScrollShadows(
            scrollState = scrollState,
            modifier = Modifier.weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // 1. Chips Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InfoChip(
                        label = "Group A",
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_stack),
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = OnNeutralContainer
                            )
                        }
                    )
                    InfoChip(
                        label = "Banglore",
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_map_pin),
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = OnNeutralContainer
                            )
                        }
                    )
                    InfoChip(label = "40 Applicants")
                }

                // 2. Job Description Card
                JobDescriptionCard()

                // 3. Note Section
                NoteSection()

                // 5. Posted Date
                Text(
                    text = "Posted on: 4 April 2026",
                    style = TextStyle(
                        fontFamily = poppins,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = TextSecondary
                    ),
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        }

        // 4. Sticky Apply Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BackgroundDefault)
                .padding(16.dp)
        ) {
            LaunchpadButton(
                text = "Apply",
                onClick = onApplyClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 1300)
@Composable
fun JobDetailsScreenPreview() {
    SBSLaunchpadTheme {
        JobDetailsScreen()
    }
}
