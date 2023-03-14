package ch.ti8m.kmmsampleapp.core.util

import kotlinx.datetime.*

object DateTimeUtil {
    fun now() =
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    fun toEpochMillis(dateTime: LocalDateTime) =
        dateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()

    fun formatNoteDate(dateTime: LocalDateTime): String {
        val month = dateTime.month.name.lowercase().take(3).replaceFirstChar { it.uppercase() }
        val day = dateTime.dayOfMonth.getTwoDigitsString()
        val year = dateTime.year
        val hour = dateTime.hour.getTwoDigitsString()
        val minute = dateTime.minute.getTwoDigitsString()
        return buildString {
            append(month)
            append(" ")
            append(day)
            append(" ")
            append(year)
            append(", ")
            append(hour)
            append(":")
            append(minute)
        }
    }

    private fun Int.getTwoDigitsString() = if(this < 10) "0${this}" else this
}