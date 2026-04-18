package com.satwik.sbslaunchpad.core.util

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
fun Instant.toReadableDate(): String {
    val dateTime = this.toLocalDateTime(TimeZone.currentSystemDefault())
    return "${dateTime.day} ${dateTime.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${dateTime.year}"
}

@OptIn(ExperimentalTime::class)
fun Instant.getRemainingTime(): String {
    val now = Clock.System.now()
    val duration = this - now
    val days = duration.inWholeDays
    
    return when {
        days > 0 -> "$days Days"
        days == 0L -> "Today"
        else -> "Expired"
    }
}
