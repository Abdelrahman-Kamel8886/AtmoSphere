package com.abdok.atmosphere.ui.screens.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abdok.atmosphere.data.models.ForecastResponse
import com.abdok.atmosphere.R
import com.abdok.atmosphere.utils.dates.DateHelper
import com.abdok.atmosphere.utils.localization.LanguageManager
import com.abdok.atmosphere.utils.SharedModel
import com.abdok.atmosphere.utils.viewHelpers.IconsMapper
import com.abdok.atmosphere.utils.getDaysForecast

@Composable
fun HourlyForecastList(
    forecast: ForecastResponse
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp),
    ) {
        val map = forecast.getDaysForecast()
        val list = map.values.first()
        items(list.size) {
            HourlyColumn(list[it])
        }
    }
}

@Composable
fun HourlyColumn(
    hour: ForecastResponse.Item0
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val h = "${DateHelper.getHourFormTime(hour.dt.toLong())}"
        val temp = "${hour.main.temp.toInt()} ${SharedModel.currentDegree}"
        Text(
            text = LanguageManager.formatNumberBasedOnLanguage(h),
            color = Color.Gray,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(end = 16.dp)
        )
        Image(
            painter = painterResource(
                IconsMapper.iconsMap.get(hour.weather.get(0).icon) ?: R.drawable.clear_sky_day
            ),
            contentDescription = "Sunset",
            modifier = Modifier.size(48.dp)
        )
        Text(
            text = LanguageManager.formatNumberBasedOnLanguage(temp),
            color = Color.DarkGray,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

    }

}

