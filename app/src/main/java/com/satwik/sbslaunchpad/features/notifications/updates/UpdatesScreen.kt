package com.satwik.sbslaunchpad.features.notifications.updates

import androidx.compose.foundation.background
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.satwik.sbslaunchpad.LocalHorizontalAppPadding
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadNotificationItem
import com.satwik.sbslaunchpad.core.designsystem.components.customShadow
import com.satwik.sbslaunchpad.core.designsystem.theme.DefaultElevation
import com.satwik.sbslaunchpad.core.designsystem.theme.ElevationStrength
import com.satwik.sbslaunchpad.core.designsystem.theme.SBSLaunchpadTheme
import com.satwik.sbslaunchpad.core.designsystem.theme.SurfaceDefault
import com.satwik.sbslaunchpad.features.auth.local_component.LazyScrollShadows
import com.satwik.sbslaunchpad.features.notifications.model.NotificationItemData

@Composable
fun UpdatesScreen(
    onNotificationClick: (NotificationItemData) -> Unit,
    modifier: Modifier = Modifier
) {
    val sampleNotifications = listOf(
        NotificationItemData(
            title = "Audit Assistant",
            company = "Deloitte",
            description = "Congratulations ! you are shortlisted for the personal interview round for the profile audit assisant. Your inteview is sheduled on 4th April google meet link will be shared to you 2 hours prior to the inteview. Goodluck",
            time = "22:00"
        ),
        NotificationItemData(
            title = "Software Engineer",
            company = "Google",
            description = "Your application for the Software Engineer role has been moved to the next stage. Please schedule your interview.",
            time = "10:30"
        ),
        NotificationItemData(
            title = "Associate Product Manager",
            company = "Microsoft",
            description = "Thank you for attending the final round. We will get back to you with the results within a week.",
            time = "14:15"
        ),
        NotificationItemData(
            title = "Applied Scientist",
            company = "Amazon",
            description = "An update regarding your interview performance has been shared by the recruitment team.",
            time = "09:00"
        ),
        NotificationItemData(
            title = "UI/UX Designer",
            company = "Adobe",
            description = "Congratulations! You have successfully cleared the design challenge for the UI/UX role.",
            time = "16:45"
        ),
        NotificationItemData(
            title = "Data Analyst",
            company = "Accenture",
            description = "The onboarding process for the Data Analyst position has started. Please upload the required documents.",
            time = "11:20"
        )
    )

    val scrollState = rememberLazyListState()

    LazyScrollShadows(scrollState = scrollState) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            state = scrollState,
            contentPadding = PaddingValues(
                horizontal = LocalHorizontalAppPadding.current,
                vertical = 24.dp
            )
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .customShadow(
                            elevation = DefaultElevation,
                            shape = RoundedCornerShape(12.dp),
                            alpha = ElevationStrength,
                        )
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceDefault)
                ) {
                    sampleNotifications.forEachIndexed { index, notification ->
                        LaunchpadNotificationItem(
                            title = notification.title,
                            company = notification.company,
                            description = notification.description,
                            time = notification.time,
                            logoUrl = notification.logoUrl,
                            showDivider = index != sampleNotifications.lastIndex,
                            onClick = { onNotificationClick(notification) }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UpdatesScreenPreview() {
    SBSLaunchpadTheme {
        UpdatesScreen(onNotificationClick = {})
    }
}
