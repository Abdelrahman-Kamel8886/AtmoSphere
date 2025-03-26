package com.abdok.atmosphere.View.Screens.Home.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abdok.atmosphere.R
import com.abdok.atmosphere.Utils.Dates.SunCycleModel
import com.abdok.atmosphere.Utils.ViewHelpers.BackgroundMapper
import com.abdok.atmosphere.View.theme.ColorTextSecondary


@Composable
fun SunCycleView(
    brush: Brush, sunRise: Long, sunSet: Long
) {
    val sunsetSunrise = SunCycleModel.getSunCycleModel(sunRise, sunSet)
    if (sunsetSunrise.isDayTime) {
        SunriseSunsetView(
            brush = brush, progress = sunsetSunrise.progress,
            sunriseTime = sunsetSunrise.sunriseTime,
            sunsetTime = sunsetSunrise.sunsetTime
        )
    } else {
        SunsetSunriseView(
            brush = brush,
            progress = sunsetSunrise.progress,
            sunriseTime = sunsetSunrise.sunriseTime,
            sunsetTime = sunsetSunrise.sunsetTime
        )
    }

}


@Preview
@Composable
fun SunriseSunsetView(
    sunsetTime: String = "6:00 PM", sunriseTime: String = "6:40 AM",
    progress: Float = 0.4f,
    brush: Brush = BackgroundMapper.getCardBackground("02d")
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(brush, shape = RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(id = R.drawable.sunrise),
                contentDescription = "Sunrise",
                tint = ColorTextSecondary
            )
            Text(
                text = stringResource(R.string.sunrise),
                color = ColorTextSecondary.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
            Text(
                text = sunriseTime,
                color = ColorTextSecondary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            SunCycleSlider(progress)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(id = R.drawable.sunset),
                contentDescription = "Sunset",
                tint = ColorTextSecondary
            )
            Text(
                text = stringResource(R.string.sunset),
                color = ColorTextSecondary.copy(alpha = 0.7f),
                fontSize = 14.sp,

                )
            Text(
                text = sunsetTime,
                color = ColorTextSecondary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Preview
@Composable
fun SunsetSunriseView(
    sunsetTime: String = "6:00 PM", sunriseTime: String = "6:40 AM",
    progress: Float = 0.4f,
    brush: Brush = BackgroundMapper.getCardBackground("01n")
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(brush, shape = RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(id = R.drawable.sunset),
                contentDescription = "Sunset",
                tint = ColorTextSecondary
            )
            Text(
                text = stringResource(R.string.sunset),
                color = Color.LightGray,
                fontSize = 14.sp
            )
            Text(
                text = sunsetTime,
                color = ColorTextSecondary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            SunCycleSlider(progress)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(id = R.drawable.sunrise),
                contentDescription = "Sunrise",
                tint = ColorTextSecondary
            )
            Text(
                text = stringResource(R.string.sunrise),
                color = Color.LightGray,
                fontSize = 14.sp
            )
            Text(
                text = sunriseTime,
                color = ColorTextSecondary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Preview
@Composable
fun SunCycleSlider(progress: Float = 0.2f) {
    Slider(
        value = progress,
        onValueChange = {},
        valueRange = 0f..1f,
        colors = SliderDefaults.colors(
            thumbColor = Color.White,
            activeTrackColor = Color.White,
            inactiveTrackColor = Color.Gray
        ), modifier = Modifier.height(30.dp)
    )
}

