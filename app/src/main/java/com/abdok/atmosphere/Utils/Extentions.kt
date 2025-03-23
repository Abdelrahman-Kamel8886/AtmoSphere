package com.abdok.atmosphere.Utils

import com.abdok.atmosphere.Data.Models.ForecastResponse
import com.abdok.atmosphere.Utils.Dates.DateHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun ForecastResponse.getDaysForecast(): Map<Int, List<ForecastResponse.Item0>> {
    val forecastMap = mutableMapOf<Int, MutableList<ForecastResponse.Item0>>()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    for (item in list) {
        val dateKey = dateFormat.format(Date(item.dt * 1000L)).replace("-", "").toInt()
        if (forecastMap[dateKey] == null) {
            forecastMap[dateKey] = mutableListOf()
        }
        forecastMap[dateKey]?.add(item)
    }
    return forecastMap.mapValues { it.value.take(8) }
}