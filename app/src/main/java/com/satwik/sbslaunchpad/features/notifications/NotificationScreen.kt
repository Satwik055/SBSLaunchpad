package com.satwik.sbslaunchpad.features.notifications

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.satwik.sbslaunchpad.LocalHorizontalAppPadding
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadTabRow
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadTopAppBar
import com.satwik.sbslaunchpad.core.designsystem.theme.SBSLaunchpadTheme
import com.satwik.sbslaunchpad.features.notifications.components.NotificationDetailSheet
import com.satwik.sbslaunchpad.features.notifications.model.NotificationSheetState
import com.satwik.sbslaunchpad.features.notifications.notices.NoticesScreen
import com.satwik.sbslaunchpad.features.notifications.updates.UpdatesScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    val tabs = listOf("Updates", "Notices")
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    var sheetContent by remember { mutableStateOf<NotificationSheetState?>(null) }
    val sheetState = rememberModalBottomSheetState()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        LaunchpadTopAppBar(
            title = "Notifications",
            onBackClick = onBackClick
        )
        Spacer(Modifier.height(20.dp))

        LaunchpadTabRow(
            selectedTabIndex = pagerState.targetPage,
            tabs = tabs,
            onTabSelected = { index ->
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            }
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Top,
            beyondViewportPageCount = 1
        ) { page ->
            when (page) {
                0 -> UpdatesScreen(
                    modifier = Modifier.fillMaxSize(),
                    onNotificationClick = { sheetContent = NotificationSheetState.Notification(it) }
                )
                1 -> NoticesScreen(
                    modifier = Modifier.fillMaxSize(),
                    onNoticeClick = { sheetContent = NotificationSheetState.Notice(it) }
                )
            }
        }
    }

    if (sheetContent != null) {
        NotificationDetailSheet(
            content = sheetContent!!,
            sheetState = sheetState,
            onDismiss = { sheetContent = null }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun NotificationScreenPreview() {
    SBSLaunchpadTheme {
        NotificationScreen(modifier = Modifier.statusBarsPadding())
    }
}
