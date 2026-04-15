package com.satwik.sbslaunchpad.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.satwik.sbslaunchpad.core.designsystem.theme.*

@Composable
fun LaunchpadNoticeItem(
    title: String,
    date: String,
    description: String,
    modifier: Modifier = Modifier,
    showDivider: Boolean = true,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
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
                    text = date,
                    style = TextStyle(
                        fontFamily = poppins,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                style = TextStyle(
                    fontFamily = poppins,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    color = TextSecondary,
                    lineHeight = 20.sp
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = SurfaceOutline,
                thickness = 1.dp
            )
        }
    }
}

@Composable
fun LaunchpadNoticeCard(
    notices: List<NoticeItemData>,
    onNoticeClick: (NoticeItemData) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .customShadow(
                elevation = DefaultElevation,
                shape = RoundedCornerShape(12.dp),
                alpha = ElevationStrength
            )
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceDefault)
    ) {
        notices.forEachIndexed { index, notice ->
            LaunchpadNoticeItem(
                title = notice.title,
                date = notice.date,
                description = notice.description,
                showDivider = index != notices.lastIndex,
                onClick = { onNoticeClick(notice) }
            )
        }
    }
}

data class NoticeItemData(
    val title: String,
    val date: String,
    val description: String
)

@Preview(showBackground = true)
@Composable
private fun LaunchpadNoticeCardPreview() {
    val sampleNotices = listOf(
        NoticeItemData(
            title = "Final Warning",
            date = "2nd April",
            description = "Students are adviced to not use fake internship certificates in there resumes, once caught they will be blacklisted permanently from the placements..."
        ),
        NoticeItemData(
            title = "Final Warning",
            date = "2nd April",
            description = "Students are adviced to not use fake internship certificates in there resumes, once caught they will be blacklisted permanently from the placements..."
        )
    )

    SBSLaunchpadTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            LaunchpadNoticeCard(
                notices = sampleNotices,
                onNoticeClick = {}
            )
        }
    }
}
