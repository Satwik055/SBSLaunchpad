package com.satwik.sbslaunchpad.features.notices

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.satwik.sbslaunchpad.features.main.LocalHorizontalAppPadding
import com.satwik.sbslaunchpad.core.designsystem.components.customShadow
import com.satwik.sbslaunchpad.core.designsystem.theme.BrandPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.DefaultElevation
import com.satwik.sbslaunchpad.core.designsystem.theme.ElevationStrength
import com.satwik.sbslaunchpad.core.designsystem.theme.SBSLaunchpadTheme
import com.satwik.sbslaunchpad.core.designsystem.theme.SurfaceDefault
import com.satwik.sbslaunchpad.core.designsystem.theme.TextSecondary
import com.satwik.sbslaunchpad.core.designsystem.theme.poppins
import com.satwik.sbslaunchpad.core.util.Result
import com.satwik.sbslaunchpad.data.notice.model.Notice
import com.satwik.sbslaunchpad.core.designsystem.components.LazyScrollShadows
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NoticesScreen(
    onNoticeClick: (Notice) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NoticeScreenViewModel = koinViewModel()
) {
    val result by viewModel.uiState.collectAsState()

    NoticesContent(
        uiState = result,
        onNoticeClick = onNoticeClick,
        onMarkAsRead = viewModel::markNoticeAsRead,
        modifier = modifier
    )
}

@Composable
fun NoticesContent(
    uiState: Result,
    onNoticeClick: (Notice) -> Unit,
    onMarkAsRead: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberLazyListState()

    Box(modifier = modifier.fillMaxSize()) {
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = BrandPrimary
                )
            }
        } else if (uiState.success) {
            @Suppress("UNCHECKED_CAST")
            val notices = uiState.successResult as? List<Notice> ?: emptyList()

            if (notices.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No notices for you",
                        fontFamily = poppins,
                        fontWeight = FontWeight.Normal,
                        color = TextSecondary
                    )
                }
            } else {
                LazyScrollShadows(scrollState = scrollState) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
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
                                notices.forEachIndexed { index, item ->
                                    NoticeItemCard(
                                        title = item.title,
                                        date = item.createdAt?.take(10) ?: "", // Simplified date for now
                                        description = item.body,
                                        isNew = !item.isRead,
                                        showDivider = index != notices.lastIndex,
                                        onClick = {
                                            onMarkAsRead(item.id)
                                            onNoticeClick(item)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else if (uiState.error.isNotEmpty()) {
            Text(
                text = uiState.error,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NoticesScreenPreview() {
    val sampleNotices = listOf(
        Notice(
            id = 1,
            title = "Final Warning",
            body = "Students are adviced to not use fake internship certificates in there resumes, once caught they will be blacklisted permanently from the placements...",
            senderId = "1",
            createdAt = "2023-10-27",
            isRead = false
        ),
        Notice(
            id = 2,
            title = "Placement Drive",
            body = "Google is visiting the campus for a placement drive on 30th October. All eligible students are requested to register.",
            senderId = "1",
            createdAt = "2023-10-26",
            isRead = true
        )
    )

    SBSLaunchpadTheme {
        NoticesContent(
            uiState = Result(success = true, successResult = sampleNotices),
            onNoticeClick = {},
            onMarkAsRead = {}
        )
    }
}
