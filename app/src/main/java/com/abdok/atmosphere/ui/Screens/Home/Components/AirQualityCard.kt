package com.abdok.atmosphere.ui.Screens.Home.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abdok.atmosphere.R
import com.abdok.atmosphere.utils.localization.LanguageManager
import com.abdok.atmosphere.utils.viewHelpers.BackgroundMapper
import com.abdok.atmosphere.ui.Theme.ColorTextSecondary

@Composable
fun CardItem(
    icon: Int, title: String = "dddd", value: String = "455", brush: Brush
) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .background(brush = brush, shape = RoundedCornerShape(12.dp))

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = ColorTextSecondary,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = title,
                color = ColorTextSecondary,
                fontSize = 14.sp
            )
            Text(
                text = value,
                color = ColorTextSecondary,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}

@Preview
@Composable
fun WeatherGrid(
    brush: Brush = BackgroundMapper.getCardBackground("01d"),
    pressure: String = "100",
    humidity: String = "20",
    visibility: String = "1000"
) {
    val items = listOf(
        Triple(
            R.drawable.humidity_icon, stringResource(R.string.humidity),
            LanguageManager.formatNumberBasedOnLanguage(humidity)
        ),
        Triple(
            R.drawable.air_icon, stringResource(R.string.air_pressure),
            LanguageManager.formatNumberBasedOnLanguage(pressure)
        ),
        Triple(
            R.drawable.outline_visibility_24,
            stringResource(R.string.visibility),
            LanguageManager.formatNumberBasedOnLanguage(visibility)
        )
    )

    LazyHorizontalGrid(
        rows = GridCells.Fixed(1),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { (icon, title, value) ->
            CardItem(icon = icon, title = title, value = value, brush)
        }
    }
}

