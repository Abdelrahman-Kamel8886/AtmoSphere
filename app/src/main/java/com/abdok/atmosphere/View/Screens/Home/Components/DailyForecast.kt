package com.abdok.atmosphere.View.Screens.Home.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.abdok.atmosphere.Data.Models.ForecastResponse
import com.abdok.atmosphere.R
import com.abdok.atmosphere.Utils.Dates.DateHelper
import com.abdok.atmosphere.Utils.LanguageManager
import com.abdok.atmosphere.Utils.SharedModel
import com.abdok.atmosphere.Utils.ViewHelpers.BackgroundMapper
import com.abdok.atmosphere.Utils.ViewHelpers.IconsMapper
import com.abdok.atmosphere.Utils.getDaysForecast
import com.abdok.atmosphere.View.Theme.ColorTextSecondary

@Composable
fun DaysForecastList(
    brush: Brush = BackgroundMapper.getCardBackground("01d"),
    forecast: ForecastResponse
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(brush, shape = RoundedCornerShape(12.dp)),
    ) {

        val map = forecast.getDaysForecast()

        map.forEach { (key, list) ->
            DayRow(daylist = list)
        }
    }

    /*repeat(8){
        DayRow()
    }*/
}

@Composable
fun DayRow(
    daylist: List<ForecastResponse.Item0>
) {

    val context = LocalContext.current

    val date = DateHelper.getDayFormTimestamp(daylist.get(0).dt.toLong(), context)
    val icon = daylist.first().weather.get(0).icon.replace("n", "d")

    var min = daylist.get(0).main.temp_min.toInt()
    var max = daylist.get(0).main.temp_max.toInt()


    for (item in daylist) {
        min = Math.min(item.main.temp_min.toInt(), min)
        max = Math.max(item.main.temp_max.toInt(), max)
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        val (description, image, degree) = createRefs()

        Column(modifier = Modifier.constrainAs(description) {
            start.linkTo(parent.start)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
        }) {
            Text(
                text = "${LanguageManager.formatNumberBasedOnLanguage(date)}",
                color = ColorTextSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

        }

        Image(
            painter = painterResource(
                id = IconsMapper.iconsMap.get(icon) ?: R.drawable.clear_sky_day
            ),
            contentDescription = "Sunset",
            modifier = Modifier
                .size(48.dp)
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start, margin = 32.dp)
                    end.linkTo(parent.end)
                }
        )

        val temp = "${min} ${SharedModel.currentDegree} / ${max} ${SharedModel.currentDegree}"
        Text(text = LanguageManager.formatNumberBasedOnLanguage(temp),
            color = ColorTextSecondary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(degree) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            }
        )

    }

}