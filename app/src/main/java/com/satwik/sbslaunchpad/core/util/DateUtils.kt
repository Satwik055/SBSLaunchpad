package com.satwik.sbslaunchpad.core.util

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
fun Instant.toReadableDate(): String {
    val dateTime = this.toLocalDateTime(TimeZone.currentSystemDefault())
    val monthName = dateTime.month.name.lowercase().replaceFirstChar { it.uppercase() }
    return "${dateTime.day} $monthName ${dateTime.year}"
}

@OptIn(ExperimentalTime::class)
fun Instant.toReadableDateTime(): String {
    val dateTime = this.toLocalDateTime(TimeZone.currentSystemDefault())
    val hour = if (dateTime.hour % 12 == 0) 12 else dateTime.hour % 12
    val amPm = if (dateTime.hour < 12) "AM" else "PM"
    val minute = dateTime.minute.toString().padStart(2, '0')
    val monthName = dateTime.month.name.lowercase().replaceFirstChar { it.uppercase() }
    return "$hour:$minute $amPm, ${dateTime.day} $monthName ${dateTime.year}"
}

@OptIn(ExperimentalTime::class)
fun Instant.getRemainingTime(): String {
    val now = Clock.System.now()
    val duration = this - now
    
    if (duration.isNegative()) return "Expired"
    
    val totalSeconds = duration.inWholeSeconds
    if (totalSeconds >= 24 * 3600) {
        val days = duration.inWholeDays
        return "$days ${if (days == 1L) "Day" else "Days"}"
    }
    
    val hours = duration.inWholeHours
    val minutes = duration.inWholeMinutes % 60
    val seconds = duration.inWholeSeconds % 60
    
    return "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
}
