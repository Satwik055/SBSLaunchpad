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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
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
    modifier: Modifier = Modifier
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
            title = "Job Description",
            content = "Head Field has been a formidable player in the outsourcing business market for more than a decade. Started with a vision to help businesses abroad recruit talent from India, our company now offers a basket of services including Recruitment Process Outsourcing, Virtual Assistance, Legal Process Outsourcing, Accounting Process Outsourcing, Digital Marketing, Interior & Architecture, Filming and Editing, IT and many more."
        )

        DetailSection(
            title = "Compensation",
            content = "CTC: ₹4,40,000 + Performance Bonus + Benifits"
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                        append("Courses: \n\n")
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

            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                        append("Active Backlog: ")
                    }
                    append("Allowed")
                },
                style = TextStyle(
                    fontFamily = poppins,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    color = TextSecondary,
                    lineHeight = 20.sp
                ),
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        DetailSection(
            title = "Skills",
            content = "* Basic understanding of Business, Marketing, International Relations, or related fields\n\n* Strong interest in Sales and Business Development\n\n* Excellent communication and interpersonal skills\n\n* Self-motivated and comfortable in a target-driven environment\n\n* Clear intent to build a career in Sales"
        )

        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, color = TextPrimary)) {
                    append("Deadline: ")
                }
                withStyle(SpanStyle(color = TextSecondary)) {
                    append("12:00 PM, 10th April 2026")
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
