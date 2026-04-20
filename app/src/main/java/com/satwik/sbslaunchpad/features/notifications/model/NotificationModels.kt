package com.satwik.sbslaunchpad.features.notifications.model

data class NoticeItemData(
    val title: String,
    val date: String,
    val description: String,
    val isNew: Boolean = false
)

data class NotificationItemData(
    val threadId: Int,
    val title: String,
    val company: String,
    val description: String,
    val time: String,
    val logoUrl: String? = null,
    val count: Int = 1
)

sealed interface NotificationSheetState {
    data class Notification(val data: NotificationItemData) : NotificationSheetState
    data class Notice(val data: NoticeItemData) : NotificationSheetState
}
