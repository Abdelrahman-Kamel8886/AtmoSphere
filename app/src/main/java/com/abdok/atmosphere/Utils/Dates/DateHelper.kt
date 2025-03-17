package com.abdok.atmosphere.Utils.Dates

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object DateHelper {

    fun getDateFromTimestamp(timestamp: Int): String {
        val date = Date(timestamp.toLong() * 1000)
        val format = SimpleDateFormat("dd MMM", Locale.getDefault())
        return format.format(date)
    }

    fun getRelativeTime(timestamp: Int): String {
        val now = System.currentTimeMillis()
        val diff = now - (timestamp.toLong() * 1000) // Convert to milliseconds

        return when {
            diff < TimeUnit.MINUTES.toMillis(1) -> "now"
            diff < TimeUnit.MINUTES.toMillis(2) -> "a minute ago"
            diff < TimeUnit.HOURS.toMillis(1) -> "${diff / TimeUnit.MINUTES.toMillis(1)} minutes ago"
            diff < TimeUnit.HOURS.toMillis(2) -> "an hour ago"
            diff < TimeUnit.DAYS.toMillis(1) -> "${diff / TimeUnit.HOURS.toMillis(1)} hours ago"
            diff < TimeUnit.DAYS.toMillis(2) -> "yesterday"
            diff < TimeUnit.DAYS.toMillis(7) -> "${diff / TimeUnit.DAYS.toMillis(1)} days ago"
            else -> {
                val date = Date(timestamp.toLong() * 1000)
                val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                format.format(date)
            }
        }
    }

}