package com.satwik.sbslaunchpad.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.satwik.sbslaunchpad.R
import com.satwik.sbslaunchpad.core.designsystem.theme.*

@Composable
fun LaunchpadJobPostCard(
    jobProfile: String,
    companyName: String,
    deadline: String,
    group: String,
    city: String,
    applicants: String,
    salary: String,
    isApplied: Boolean,
    modifier: Modifier = Modifier,
    companyLogoUrl: String? = null,
    onClick: () -> Unit = {},
) {
    Card(
        modifier = modifier
            .customShadow(
                elevation = DefaultElevation,
                shape = RoundedCornerShape(5.dp),
                alpha = ElevationStrength
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDefault),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        onClick = onClick,
    ) {
        Column {
            JobPostCardBody(
                jobProfile = jobProfile,
                companyName = companyName,
                deadline = deadline,
                group = group,
                city = city,
                applicants = applicants,
                salary = salary,
                companyLogoUrl = companyLogoUrl
            )

            if (isApplied) {
                AppliedBanner()
            }
        }
    }
}

@Composable
private fun JobPostCardBody(
    jobProfile: String,
    companyName: String,
    deadline: String,
    group: String,
    city: String,
    applicants: String,
    salary: String,
    companyLogoUrl: String?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f),
            ) {
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(NeutralContainer),
                    contentAlignment = Alignment.Center,
                ) {
                    if (companyLogoUrl != null) {
                        AsyncImage(
                            model = companyLogoUrl,
                            contentDescription = "Company logo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                            contentDescription = "Company logo",
                            tint = OnNeutralContainer,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text(
                        text = jobProfile,
                        style = TextStyle(
                            fontFamily = poppins,
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp
                        ),
                        color = TextPrimary,
                    )
                    Text(
                        text = companyName,
                        style = TextStyle(
                            fontFamily = poppins,
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp
                        ),
                        color = TextSecondary,
                    )
                }
            }

            DeadlineBadge(deadline = deadline)
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            InfoChip(
                label = group,
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_stack),
                        contentDescription = "Group",
                        tint = OnNeutralContainer,
                        modifier = Modifier.size(14.dp),
                    )
                },
            )

            InfoChip(
                label = city,
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_map_pin),
                        contentDescription = "Location",
                        tint = OnNeutralContainer,
                        modifier = Modifier.size(12.dp),
                    )
                },
            )

            InfoChip(label = applicants)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontFamily = poppins, fontWeight = FontWeight.Medium, fontSize = 15.sp, color = TextPrimary)) {
                        append(salary)
                    }
                    withStyle(SpanStyle(fontFamily = poppins, fontWeight = FontWeight.Normal, fontSize = 14.sp, color = TextSecondary)) {
                        append("/yr")
                    }
                },
            )

            Icon(
                painter = painterResource(R.drawable.ic_carret_right),
                contentDescription = "View details",
                tint = TextSecondary,
                modifier = Modifier.size(10.dp, 20.dp),
            )
        }
    }
}

@Composable
private fun DeadlineBadge(deadline: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFFFDADA))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(Color.Red),
        )
        Text(
            text = deadline,
            style = TextStyle(
                fontFamily = poppins,
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp
            ),
            color = Color.Red,
        )
    }
}

@Composable
private fun InfoChip(
    label: String,
    leadingIcon: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .height(32.dp)
            .clip(RoundedCornerShape(19.dp))
            .background(NeutralContainer)
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        leadingIcon?.invoke()
        Text(
            text = label,
            style = TextStyle(
                fontFamily = poppins,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp
            ),
            color = OnNeutralContainer,
        )
    }
}

@Composable
private fun AppliedBanner() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(49.dp)
            .background(BrandPrimary)
            .padding(horizontal = 21.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_check),
            contentDescription = "Applied",
            tint = BrandOnPrimary,
            modifier = Modifier.size(18.dp, 13.dp),
        )
        Text(
            text = "Applied",
            style = TextStyle(
                fontFamily = poppins,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp
            ),
            color = BrandOnPrimary,
        )
    }
}

@Preview
@Composable
fun LaunchpadJobPostCardPreview() {
    SBSLaunchpadTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            LaunchpadJobPostCard(
                jobProfile = "Audit Assistant",
                companyName = "Deloitte",
                deadline = "3 Days",
                group = "Group B",
                city = "Delhi",
                applicants = "42 Applicants",
                salary = "₹ 4,40,000",
                isApplied = true,
            )
        }
    }
}
