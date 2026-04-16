package com.satwik.sbslaunchpad.features.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.satwik.sbslaunchpad.R
import com.satwik.sbslaunchpad.core.designsystem.components.InfoChip
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadButton
import com.satwik.sbslaunchpad.core.designsystem.components.customShadow
import com.satwik.sbslaunchpad.core.designsystem.theme.*

@Composable
fun JobDetailsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onApplyClick: () -> Unit = {},
    companyLogoUrl: String? = null
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            JobDetailsTopBar(
                title = "Audit Assistant",
                company = "Deloitte",
                onBackClick = onBackClick,
                companyLogoUrl = companyLogoUrl
            )
        },
        containerColor = BackgroundDefault
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Chips Row
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

            // Main Job Description Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .customShadow(
                        elevation = DefaultElevation,
                        shape = RoundedCornerShape(12.dp),
                        alpha = ElevationStrength
                    )
                    .clip(RoundedCornerShape(24.dp))
                    .background(SurfaceDefault)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DetailSection(
                    title = "Job Description",
                    content = "Head Field has been a formidable player in the outsourcing business market for more than a decade. Started with a vision to help businesses abroad recruit talent from India, our company now offers a basket of services including Recruitment Process Outsourcing, Virtual Assistance, Legal Process Outsourcing, Accounting Process Outsourcing, Digital Marketing, Interior & Architecture, Filming and Editing, IT and many more."
                )

                DetailSection(
                    title = "Compensation",
                    content = "CTC: ₹4,40,000 + Performance Bonus + Benifits"
                )

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Eligibility",
                        style = TextStyle(
                            fontFamily = poppins,
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.Medium)) {
                                append("Courses: \n")
                            }
                            append("B.Com(Hons), B.Com(Program), B.A (Economics Hons, B.A(English)")
                        },
                        style = TextStyle(
                            fontFamily = poppins,
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp,
                            color = TextSecondary,
                            lineHeight = 20.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.Medium)) {
                                append("Active Backlog: ")
                            }
                            append("Allowed")
                        },
                        style = TextStyle(
                            fontFamily = poppins,
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    )
                }

                DetailSection(
                    title = "Skills:",
                    content = "* Basic understanding of Business, Marketing, International Relations, or related fields\n\n* Strong interest in Sales and Business Development\n\n* Excellent communication and interpersonal skills\n\n* Self-motivated and comfortable in a target-driven environment\n\n* Clear intent to build a career in Sales"
                )

                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Medium, color = TextPrimary)) {
                            append("Deadline: ")
                        }
                        withStyle(SpanStyle(fontWeight = FontWeight.Normal, color = TextSecondary)) {
                            append("12:00 PM, 10th April 2025")
                        }
                    },
                    style = TextStyle(
                        fontFamily = poppins,
                        fontSize = 14.sp
                    )
                )

                DetailSection(
                    title = "Selection Process",
                    content = "Personal Interview (HR + Business Panel)"
                )
            }

            // Note Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFFE5E5))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_info),
                        contentDescription = null,
                        tint = Color(0xFFFF5252),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Note",
                        style = TextStyle(
                            fontFamily = poppins,
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp,
                            color = Color(0xFFFF5252)
                        )
                    )
                }
                Text(
                    text = "Only students who are genuinely interested in this opportunity should apply. Any student who withdraws or backs out at any stage of the process will be blacklisted from future placement opportunities.",
                    style = TextStyle(
                        fontFamily = poppins,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = Color(0xFFFF5252),
                        lineHeight = 18.sp
                    )
                )
            }

            LaunchpadButton(
                text = "Apply",
                onClick = onApplyClick,
                modifier = Modifier.padding(top = 8.dp)
            )

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

@Composable
fun JobDetailsTopBar(
    title: String,
    company: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    companyLogoUrl: String? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                painter = painterResource(id = R.drawable.ic_carret2),
                contentDescription = "Back",
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Logo
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

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = title,
                style = TextStyle(
                    fontFamily = poppins,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = TextPrimary
                )
            )
            Text(
                text = company,
                style = TextStyle(
                    fontFamily = poppins,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = TextSecondary
                )
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
