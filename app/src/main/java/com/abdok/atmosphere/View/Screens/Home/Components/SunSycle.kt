package com.abdok.atmosphere.View.Screens.Home.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NightlightRound
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abdok.atmosphere.R
import com.abdok.atmosphere.Utils.Dates.SunCycleModel
import com.abdok.atmosphere.Utils.ViewHelpers.BackgroundMapper
import com.abdok.atmosphere.View.Theme.ColorTextSecondary


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
                .width(160.dp)
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            DaySlider(progress)
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


@Preview
@Composable
fun SunsetSunriseView(
    sunsetTime: String = "6:00 PM",
    sunriseTime: String = "6:40 AM",
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

        // Use a fixed width instead of weight
        Box(
            modifier = Modifier
                .width(160.dp)
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            NightSlider(progress)
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

@Composable
fun CustomSlider(
    progress: Float,
    icon: ImageVector,
    iconTint: Color,
    iconBackground: Color,
    activeColor: Color,
    inactiveColor: Color,
    width: Dp = 200.dp
) {
    val offsetX = with(LocalDensity.current) {
        ((progress * (width - 24.dp).toPx()) - 12.dp.toPx()).toDp()
    }

    Box(
        modifier = Modifier
            .width(width) // Use the provided width
            .height(40.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        // Inactive Track
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(50))
                .background(inactiveColor)
        )
        // Active Track
        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .height(8.dp)
                .clip(RoundedCornerShape(50))
                .background(activeColor)
        )
        // Icon (Thumb)
        Box(
            modifier = Modifier
                .offset(x = offsetX)
                .size(24.dp)
                .clip(CircleShape)
                .background(iconBackground)
                .border(2.dp, iconBackground, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NightSlider(progress: Float = 0.7f) {
    CustomSlider(
        progress = progress,
        icon = Icons.Default.NightlightRound,
        iconTint = Color.DarkGray,
        iconBackground = Color.White,
        activeColor = Color.White,
        inactiveColor = Color.LightGray,
        width = 160.dp
    )
}

@Preview(showBackground = true)
@Composable
fun DaySlider(progress: Float = 0.7f) {
    CustomSlider(
        progress = progress,
        icon = Icons.Default.WbSunny,
        iconTint = Color(0xFFFF7043),
        iconBackground = Color.White,
        activeColor = Color.White,
        inactiveColor = Color.LightGray,
        width = 160.dp
    )
}

