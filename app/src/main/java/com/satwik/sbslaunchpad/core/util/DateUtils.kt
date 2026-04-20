package com.satwik.sbslaunchpad.core.util

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale
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
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalTime::class)
fun String.toFormattedTimestamp(): String {
    val odt = OffsetDateTime.parse(this)

    val hour = odt.hour
    val minute = odt.minute
    val amPm = if (hour < 12) "AM" else "PM"
    val hour12 = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }

    val dayFormatter = DateTimeFormatter.ofPattern("d MMMM, yyyy", Locale.ENGLISH)
    val datePart = odt.format(dayFormatter)

    val timePart = if (minute == 0) "${hour12}${amPm}"
    else "${hour12}:${minute.toString().padStart(2, '0')}${amPm}"

    return "$timePart $datePart"
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
