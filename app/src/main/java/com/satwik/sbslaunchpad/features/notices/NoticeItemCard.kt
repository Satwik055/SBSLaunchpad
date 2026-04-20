package com.satwik.sbslaunchpad.features.notices

import androidx.compose.foundation.background
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
import com.satwik.sbslaunchpad.core.designsystem.components.customShadow
import com.satwik.sbslaunchpad.core.designsystem.theme.*
import com.satwik.sbslaunchpad.features.notifications.model.NoticeItemData

@Composable
fun NoticeItemCard(
    title: String,
    date: String,
    description: String,
    modifier: Modifier = Modifier,
    isNew: Boolean = false,
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
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    modifier = Modifier.weight(1f),
                    style = TextStyle(
                        fontFamily = poppins,
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp,
                        color = TextPrimary
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.width(30.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = date,
                        style = TextStyle(
                            fontFamily = poppins,
                            fontWeight = FontWeight.Normal,
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    )

                    if (isNew) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(BrandPrimary)
                                .padding(horizontal = 10.dp, vertical = 2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "New",
                                style = TextStyle(
                                    fontFamily = poppins,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 10.sp,
                                    color = BrandOnPrimary
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

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

        Spacer(modifier = Modifier.height(16.dp))

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
            NoticeItemCard(
                title = notice.title,
                date = notice.date,
                description = notice.description,
                isNew = notice.isNew,
                showDivider = index != notices.lastIndex,
                onClick = { onNoticeClick(notice) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LaunchpadNoticeCardPreview() {
    val sampleNotices = listOf(
        NoticeItemData(
            title = "Final Warning",
            date = "2nd April",
            description = "Students are adviced to not use fake internship certificates in there resumes, once caught they will be blacklisted permanently from the placements...",
            isNew = true
        ),
        NoticeItemData(
            title = "Final Warning",
            date = "2nd April",
            description = "Students are adviced to not use fake internship certificates in there resumes, once caught they will be blacklisted permanently from the placements...",
            isNew = false
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
