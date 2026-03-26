package io.aethibo.combatcoach.shared.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun startOfWeekEpoch(): Long {
    val cal = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return cal.timeInMillis
}

fun startOfMonthEpoch(): Long {
    val cal = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return cal.timeInMillis
}

fun Long.toDisplayDate(): String {
    val sdf = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
    return sdf.format(Date(this))
}

fun Long.toDisplayDateTime(): String {
    val sdf = SimpleDateFormat("MMM d, yyyy · h:mm a", Locale.getDefault())
    return sdf.format(Date(this))
}

fun Long.toRelativeDate(): String {
    val now = System.currentTimeMillis()
    val diff = now - this
    val days = diff / (1000 * 60 * 60 * 24)
    return when {
        days == 0L -> "Today"
        days == 1L -> "Yesterday"
        days < 7 -> "$days days ago"
        days < 30 -> "${days / 7} weeks ago"
        else -> this.toDisplayDate()
    }
}

fun Int.toMinutesSeconds(): String {
    val m = this / 60
    val s = this % 60
    return if (m > 0) "${m}m ${s}s" else "${s}s"
}

fun Int.toTimerDisplay(): String {
    val m = this / 60
    val s = this % 60
    return "%02d:%02d".format(m, s)
}
