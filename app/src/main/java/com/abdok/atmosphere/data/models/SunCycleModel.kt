package com.abdok.atmosphere.data.models

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class SunCycleModel(
    val sunriseTime: String,
    val sunsetTime: String,
    val progress: Float,
    val isDayTime: Boolean
){
    companion object{
        fun getSunCycleModel(sunriseTimestamp: Long, sunsetTimestamp: Long): SunCycleModel {
            val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())

            val sunriseTime = timeFormat.format(Date(sunriseTimestamp * 1000))
            val sunsetTime = timeFormat.format(Date(sunsetTimestamp * 1000))

            val currentTime = System.currentTimeMillis() / 1000

            val progress: Float
            val isDayTime: Boolean

            if (currentTime in sunriseTimestamp..sunsetTimestamp) {
                progress = (currentTime - sunriseTimestamp).toFloat() / (sunsetTimestamp - sunriseTimestamp).toFloat()
                isDayTime = true
            } else {
                if (currentTime < sunriseTimestamp) {
                    progress = (currentTime + (86400 - sunsetTimestamp)).toFloat() /
                            (sunriseTimestamp + (86400 - sunsetTimestamp)).toFloat()
                } else {
                    progress = (currentTime - sunsetTimestamp).toFloat() /
                            ((sunriseTimestamp + 86400) - sunsetTimestamp).toFloat()
                }
                isDayTime = false
            }

            return SunCycleModel(
                sunriseTime = sunriseTime,
                sunsetTime = sunsetTime,
                progress = progress,
                isDayTime = isDayTime
            )
        }
    }
}

