package com.abdok.atmosphere.Utils.Dates

import android.content.Context
import com.abdok.atmosphere.R
import com.abdok.atmosphere.Utils.LanguageManager
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object DateHelper {

    fun getDateFromTimestamp(timestamp: Int): String {
        val date = Date(timestamp.toLong() * 1000)
        val format = SimpleDateFormat("dd MMM", Locale.getDefault())
        return format.format(date)
    }

    fun getDayFormTimestamp(timestamp: Long , context: Context): String {
        val date = Instant.ofEpochSecond(timestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        val today = LocalDate.now()
        val tomorrow = today.plusDays(1)

        return when (date) {

            
            today -> "${date.format(DateTimeFormatter.ofPattern("dd MMM"))} ${context.getString(R.string.today0)}"
            tomorrow -> "${date.format(DateTimeFormatter.ofPattern("dd MMM"))} ${context.getString(R.string.tomorrow)}"
            else -> date.format(DateTimeFormatter.ofPattern("dd MMM EEE"))
        }
    }

    fun getHourFormTime(timestamp: Long): String {
        val time = Instant.ofEpochSecond(timestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalTime()

        return time.format(DateTimeFormatter.ofPattern("hh:mm a"))
    }

    fun getRelativeTime(timestamp: Int , context: Context): String {
        val now = System.currentTimeMillis()
        val diff = now - (timestamp.toLong() * 1000) // Convert to milliseconds

        return when {
            diff < TimeUnit.MINUTES.toMillis(1) -> context.getString(R.string.now)
            diff < TimeUnit.MINUTES.toMillis(2) -> context.getString(R.string.a_minute_ago)
            diff < TimeUnit.HOURS.toMillis(1) ->
                context.getString(
                    R.string.minutes_ago,
                    LanguageManager.formatNumberBasedOnLanguage("${diff / TimeUnit.MINUTES.toMillis(1)}")
                )
            
            diff < TimeUnit.HOURS.toMillis(2) -> context.getString(R.string.an_hour_ago)
            
            diff < TimeUnit.DAYS.toMillis(1) ->
                context.getString(
                    R.string.hours_ago,
                    LanguageManager.formatNumberBasedOnLanguage("${diff / TimeUnit.HOURS.toMillis(1)}")
                )
            
            diff < TimeUnit.DAYS.toMillis(2) -> context.getString(R.string.yesterday)
            
            diff < TimeUnit.DAYS.toMillis(7) ->
                context.getString(
                    R.string.days_ago,
                    LanguageManager.formatNumberBasedOnLanguage("${diff / TimeUnit.DAYS.toMillis(1)}")
                )
            else -> {
                val date = Date(timestamp.toLong() * 1000)
                val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                format.format(date)
            }
        }
    }

    fun getDayAndTimeFromTimestamp(timestamp: Long): String {
        val date = Date(timestamp*1000)
        val dateFormat = SimpleDateFormat("EEE d MMM HH:mm a", Locale.getDefault())
        return dateFormat.format(date)
    }

}