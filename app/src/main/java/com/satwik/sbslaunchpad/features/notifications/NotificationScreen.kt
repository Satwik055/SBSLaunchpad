package com.satwik.sbslaunchpad.features.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.satwik.sbslaunchpad.R
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadTabRow
import com.satwik.sbslaunchpad.core.designsystem.components.NoticeItemData
import com.satwik.sbslaunchpad.core.designsystem.components.NotificationItemData
import com.satwik.sbslaunchpad.core.designsystem.theme.IconPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.NeutralContainer
import com.satwik.sbslaunchpad.core.designsystem.theme.OnNeutralContainer
import com.satwik.sbslaunchpad.core.designsystem.theme.SBSLaunchpadTheme
import com.satwik.sbslaunchpad.core.designsystem.theme.SurfaceDefault
import com.satwik.sbslaunchpad.core.designsystem.theme.SurfaceOutline
import com.satwik.sbslaunchpad.core.designsystem.theme.TextPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.TextSecondary
import com.satwik.sbslaunchpad.core.designsystem.theme.poppins
import com.satwik.sbslaunchpad.features.notifications.notices.NoticesScreen
import com.satwik.sbslaunchpad.features.notifications.updates.UpdatesScreen
import kotlinx.coroutines.launch

private sealed interface NotificationSheetState {
    data class Notification(val data: NotificationItemData) : NotificationSheetState
    data class Notice(val data: NoticeItemData) : NotificationSheetState
}

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
        NotificationTopBar(onBackClick = onBackClick)

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

@Composable
private fun NotificationTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_carret2),
            contentDescription = "Back",
            tint = IconPrimary,
            modifier = Modifier
                .size(16.dp)
                .clickable { onBackClick() }
        )
        Text(
            text = "Notifications",
            fontFamily = poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            color = TextPrimary,
            modifier = Modifier.padding(start = 24.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificationDetailSheet(
    content: NotificationSheetState,
    sheetState: SheetState,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SurfaceDefault,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(start = 24.dp, end = 24.dp, bottom = 48.dp)
        ) {
            when (content) {
                is NotificationSheetState.Notification -> NotificationDetailContent(content.data)
                is NotificationSheetState.Notice -> NoticeDetailContent(content.data)
            }
        }
    }
}

@Composable
private fun NotificationDetailContent(notification: NotificationItemData) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(NeutralContainer),
            contentAlignment = Alignment.Center
        ) {
            if (notification.logoUrl != null) {
                AsyncImage(
                    model = notification.logoUrl,
                    contentDescription = "Company logo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                    contentDescription = "Company logo",
                    tint = OnNeutralContainer,
                    modifier = Modifier.size(32.dp),
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = notification.title,
                    fontFamily = poppins,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = TextPrimary
                )
                Text(
                    text = notification.time,
                    fontFamily = poppins,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = TextSecondary
                )
            }
            Text(
                text = notification.company,
                fontFamily = poppins,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = TextSecondary
            )
        }
    }

    Spacer(modifier = Modifier.height(24.dp))

    Text(
        text = notification.description,
        fontFamily = poppins,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = TextSecondary,
        lineHeight = 22.sp,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun NoticeDetailContent(notice: NoticeItemData) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = notice.title,
            fontFamily = poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = TextPrimary
        )
        Text(
            text = notice.date,
            fontFamily = poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = TextSecondary
        )
    }

    Spacer(modifier = Modifier.height(16.dp))
    HorizontalDivider(color = SurfaceOutline, thickness = 1.dp)
    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = notice.description,
        fontFamily = poppins,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = TextSecondary,
        lineHeight = 22.sp,
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
private fun NotificationScreenPreview() {
    SBSLaunchpadTheme {
        NotificationScreen()
    }
}
