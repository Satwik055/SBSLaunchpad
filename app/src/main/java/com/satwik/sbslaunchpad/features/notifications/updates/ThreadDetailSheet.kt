package com.satwik.sbslaunchpad.features.notifications.updates

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.satwik.sbslaunchpad.core.designsystem.theme.BrandPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.NeutralContainer
import com.satwik.sbslaunchpad.core.designsystem.theme.OnNeutralContainer
import com.satwik.sbslaunchpad.core.designsystem.theme.SBSLaunchpadTheme
import com.satwik.sbslaunchpad.core.designsystem.theme.SurfaceDefault
import com.satwik.sbslaunchpad.core.designsystem.theme.TextPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.TextSecondary
import com.satwik.sbslaunchpad.core.designsystem.theme.poppins
import com.satwik.sbslaunchpad.core.util.Result
import com.satwik.sbslaunchpad.data.thread.Message
import com.satwik.sbslaunchpad.features.notifications.model.NotificationItemData
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThreadDetailSheet(
    notification: NotificationItemData,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    viewModel: ThreadViewModel = koinViewModel()
) {
    val result by viewModel.getMessages(notification.threadId).collectAsState(initial = Result(isLoading = true))

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SurfaceDefault,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 48.dp),
            contentAlignment = Alignment.Center
        ) {
            if (result.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(vertical = 32.dp),
                    color = BrandPrimary
                )
            } else {
                @Suppress("UNCHECKED_CAST")
                val messages = result.successResult as? List<Message> ?: emptyList()
                ThreadDetailContent(
                    notification = notification,
                    messages = messages
                )
            }
        }
    }
}

@Composable
fun ThreadDetailContent(
    notification: NotificationItemData,
    messages: List<Message>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 48.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp),
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
                            painter = painterResource(id = R.drawable.ic_menu_gallery),
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
                    Text(
                        text = notification.title,
                        fontFamily = poppins,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = notification.company,
                        fontFamily = poppins,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                }
            }
        }
        itemsIndexed(messages) { index, message ->
            ThreadMessageItem(
                content = message.content,
                sender = message.admin?.name ?: "System",
                time = message.createdAt ?: "",
                isNew = !message.isRead,
                showConnector = index != messages.lastIndex
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ThreadDetailContentPreview() {
    SBSLaunchpadTheme {
        ThreadDetailContent(
            notification = NotificationItemData(
                threadId = 1,
                title = "Audit Assistant",
                company = "Deloitte",
                description = "Congratulations ! you are shortlisted for the personal interview round for the profile audit assisant...",
                time = "6 April, 2025",
                logoUrl = null,
                count = 2
            ),
            messages = emptyList()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ThreadDetailSheetPreview() {
    SBSLaunchpadTheme {
        ThreadDetailSheet(
            notification = NotificationItemData(
                threadId = 1,
                title = "Audit Assistant",
                company = "Deloitte",
                description = "Congratulations ! you are shortlisted for the personal interview round for the profile audit assisant...",
                time = "6 April, 2025",
                logoUrl = null,
                count = 2
            ),
            sheetState = rememberModalBottomSheetState(),
            onDismiss = {}
        )
    }
}
