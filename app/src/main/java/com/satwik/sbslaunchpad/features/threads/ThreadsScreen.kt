package com.satwik.sbslaunchpad.features.threads

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.satwik.sbslaunchpad.features.main.LocalHorizontalAppPadding
import com.satwik.sbslaunchpad.core.designsystem.components.customShadow
import com.satwik.sbslaunchpad.core.designsystem.theme.BrandPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.DefaultElevation
import com.satwik.sbslaunchpad.core.designsystem.theme.ElevationStrength
import com.satwik.sbslaunchpad.core.designsystem.theme.SBSLaunchpadTheme
import com.satwik.sbslaunchpad.core.designsystem.theme.SurfaceDefault
import com.satwik.sbslaunchpad.features.auth.local_component.LazyScrollShadows
import com.satwik.sbslaunchpad.features.notifications.model.NotificationItemData
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UpdatesScreen(
    onNotificationClick: (NotificationItemData) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ThreadViewModel = koinViewModel()
) {
    val result by viewModel.uiState.collectAsState()
    val scrollState = rememberLazyListState()

    Box(modifier = modifier.fillMaxSize()) {
        if (result.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = BrandPrimary
            )
        } else if (result.success) {
            val threadsWithPosts = result.successResult as? List<ThreadWithPost> ?: emptyList()

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
                            threadsWithPosts.forEachIndexed { index, item ->
                                val thread = item.thread
                                val post = item.post
                                ThreadItemCard(
                                    title = post?.jobProfile ?: "Update",
                                    companyName = post?.companyName ?: "Placement Cell",
                                    lastMessageText = thread.lastMessage,
                                    logoUrl = post?.companyLogoUrl,
                                    unreadCount = thread.unreadCount,
                                    showDivider = index != threadsWithPosts.lastIndex,
                                    onClick = {
                                        onNotificationClick(
                                            NotificationItemData(
                                                threadId = thread.id,
                                                title = post?.jobProfile ?: "Update",
                                                company = post?.companyName ?: "Placement Cell",
                                                description = thread.lastMessage,
                                                time = thread.lastMessageTime,
                                                logoUrl = post?.companyLogoUrl,
                                                count = thread.unreadCount
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        } else if (result.error.isNotEmpty()) {
            Text(
                text = result.error,
                modifier = Modifier.align(Alignment.Center)
            )
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
