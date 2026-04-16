package com.satwik.sbslaunchpad.features.notifications.notices

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.satwik.sbslaunchpad.LocalHorizontalAppPadding
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadNoticeItem
import com.satwik.sbslaunchpad.core.designsystem.components.customShadow
import com.satwik.sbslaunchpad.core.designsystem.theme.DefaultElevation
import com.satwik.sbslaunchpad.core.designsystem.theme.ElevationStrength
import com.satwik.sbslaunchpad.core.designsystem.theme.SurfaceDefault
import com.satwik.sbslaunchpad.features.auth.local_component.LazyScrollShadows
import com.satwik.sbslaunchpad.features.notifications.model.NoticeItemData

@Composable
fun NoticesScreen(
    onNoticeClick: (NoticeItemData) -> Unit,
    modifier: Modifier = Modifier
) {
    val sampleNotices = listOf(
        NoticeItemData(
            title = "Final Warning",
            date = "2nd April",
            description = "Students are advised to not use fake internship certificates in their resumes. Once caught, they will be blacklisted permanently from the placements."
        ),
        NoticeItemData(
            title = "Holiday Notice",
            date = "5th April",
            description = "The college will remain closed on 10th April on account of Eid-ul-Fitr. Regular classes will resume from 11th April."
        ),
        NoticeItemData(
            title = "Fee Payment Deadline",
            date = "8th April",
            description = "This is a reminder that the last date for semester fee payment is 15th April. A late fee will be applicable after the deadline."
        ),
        NoticeItemData(
            title = "Workshop on AI/ML",
            date = "10th April",
            description = "A two-day workshop on Artificial Intelligence and Machine Learning will be held in the main auditorium on 18th and 19th April."
        ),
        NoticeItemData(
            title = "Library Timings Update",
            date = "12th April",
            description = "The central library will now remain open until 10:00 PM on weekdays to assist students during the examination period."
        ),
        NoticeItemData(
            title = "Sports Meet 2024",
            date = "15th April",
            description = "Registrations are now open for the Annual Sports Meet 2024. Interested students can sign up at the sports office."
        ),
        NoticeItemData(
            title = "Placement Drive: TCS",
            date = "18th April",
            description = "Tata Consultancy Services (TCS) is conducting a campus recruitment drive for final year students on 25th April. Register on the portal."
        ),
        NoticeItemData(
            title = "Guest Lecture: Web3",
            date = "20th April",
            description = "Join us for an insightful guest lecture on Web3 and the future of the internet by industry experts in the seminar hall."
        ),
        NoticeItemData(
            title = "End Semester Exams",
            date = "22nd April",
            description = "The tentative schedule for the end-semester examinations has been posted on the college website. Please check for updates."
        ),
        NoticeItemData(
            title = "Annual Fest: 'Aura'",
            date = "25th April",
            description = "Get ready for 'Aura', our annual cultural fest! Auditions for various events start next week at the student activity center."
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
                    sampleNotices.forEachIndexed { index, notice ->
                        LaunchpadNoticeItem(
                            title = notice.title,
                            date = notice.date,
                            description = notice.description,
                            showDivider = index != sampleNotices.lastIndex,
                            onClick = { onNoticeClick(notice) }
                        )
                    }
                }
            }
        }
    }
}
