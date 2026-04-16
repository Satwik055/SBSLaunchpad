package com.satwik.sbslaunchpad.features.notifications.model

data class NoticeItemData(
    val title: String,
    val date: String,
    val description: String
)

data class NotificationItemData(
    val title: String,
    val company: String,
    val description: String,
    val time: String,
    val logoUrl: String? = null
)

sealed interface NotificationSheetState {
    data class Notification(val data: NotificationItemData) : NotificationSheetState
    data class Notice(val data: NoticeItemData) : NotificationSheetState
}
