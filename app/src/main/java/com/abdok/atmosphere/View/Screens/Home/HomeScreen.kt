package com.abdok.atmosphere.View.Screens.Home

import android.location.Location
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abdok.atmosphere.Data.DataSources.RemoteDataSource
import com.abdok.atmosphere.Data.Remote.RetroConnection
import com.abdok.atmosphere.Data.Repository.Repository

import androidx.lifecycle.viewmodel.compose.*
import com.abdok.atmosphere.Data.Models.CombinedWeatherData
import com.abdok.atmosphere.Data.Models.ForecastResponse
import com.abdok.atmosphere.Data.Models.WeatherResponse
import com.abdok.atmosphere.Data.Response
import com.abdok.atmosphere.R
import com.abdok.atmosphere.View.theme.ColorTextSecondary
import com.abdok.atmosphere.View.theme.ColorTextSecondaryVariant
import com.abdok.atmosphere.View.theme.colorSunText
import com.abdok.atmosphere.Utils.ViewHelpers.BackgroundMapper
import com.abdok.atmosphere.Utils.Constants
import com.abdok.atmosphere.Utils.CountryHelper
import com.abdok.atmosphere.Utils.Dates.DateHelper
import com.abdok.atmosphere.Utils.Dates.SunCycleModel
import com.abdok.atmosphere.Utils.ViewHelpers.IconsMapper
import com.abdok.atmosphere.Utils.SharedModel
import com.abdok.atmosphere.Utils.getDaysForecast
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun HomeScreen(viewModel: HomeViewModel , location: Location) {

    val loc1 = 30.666733 to 31.169271
    val loc2 = 50.666733 to 8.468946
    val loc3 = 4.666733 to 8.468946
    val loc4 = 60.666733 to 11.169271
    val loc5 = 4.666733 to 36.169271

    val loc = loc1

    LaunchedEffect(Unit) {
        /*viewModel.getWeatherAndForecastLatLon(loc.first, loc.first)*/
        viewModel.getWeatherAndForecastLatLon(location.latitude, location.longitude)
    }

    val weatherDataState = viewModel.combinedWeatherData.collectAsStateWithLifecycle()
    val messageState = viewModel.error.observeAsState()


    when(weatherDataState.value){
        is Response.Loading -> {
            Box (Modifier.fillMaxSize() , contentAlignment = Alignment.Center){
                CircularProgressIndicator()
            }
        }
        is Response.Error -> {
            val message = (weatherDataState.value as Response.Error).exception
            Text(text = message)
        }
        is Response.Success -> {
            val data = (weatherDataState.value as Response.Success).data
            DrawHome(combinedWeatherData = data)
        }
    }
}

@Composable
fun DrawHome(combinedWeatherData:CombinedWeatherData){
    Box(modifier = Modifier.fillMaxSize()) {
/*        // Background GIF
        *//*RainEffectBackground(
            modifier = Modifier
                .matchParentSize()
                .zIndex(0f) // Set background layer to lowest
        )*/

        Column(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1f)
                .verticalScroll(rememberScrollState())
        ) {
            combinedWeatherData.weatherResponse?.let {
                val cardBrush = BackgroundMapper.getCardBackground(it.weather[0].icon)
                val screenBrush = BackgroundMapper.getScreenBackground(it.weather[0].icon)
                SharedModel.screenBackground.value = screenBrush

                Column {
                    Spacer(modifier = Modifier.height(32.dp))
                    TopView(it.name, CountryHelper.getCountryNameFromCode(it.sys.country) ?: "")
                    Spacer(modifier = Modifier.height(8.dp))
                    WeatherCard(it)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = stringResource(R.string.today) , modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp), fontWeight = FontWeight.Bold , fontSize = 24.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    HourlyForecastList(combinedWeatherData?.forecastResponse!!)
                    Spacer(modifier = Modifier.height(16.dp))
                    WindCard(brush = cardBrush , value = it.wind.speed , degree = it.wind.deg.toFloat())
                    Spacer(modifier = Modifier.height(8.dp))
                    WeatherGrid(cardBrush
                        , humidity = "${it.main.humidity}%"
                        , visibility = "${it.visibility} ${Constants.visibilityUnit}"
                        , pressure = "${it.main.pressure} hpa"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    SunCycleView(
                        brush = cardBrush,
                        sunRise = it.sys.sunrise.toLong(),
                        sunSet = it.sys.sunset.toLong()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = stringResource(R.string._5_days_forecast) , modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp), fontWeight = FontWeight.Bold , fontSize = 24.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    DaysForecastList(brush = cardBrush , forecast = combinedWeatherData?.forecastResponse!!)
                    Spacer(modifier = Modifier.height(100.dp))
                }

            }
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun RainEffectBackground(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Load the GIF
        GlideImage(
            contentScale = ContentScale.FillHeight,
            model = R.drawable.ra, // Use the GIF in res/drawable
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.5f)
        )
    }
}

@Composable
fun TopView(
    city: String = "Zefta",
    country: String = "Egypt"
) {

    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 48.dp, start = 16.dp)
    ) {

        Text(
            text = "$city,",
            fontWeight = FontWeight.SemiBold, fontSize = 30.sp
        )

        Text(
            text = country,
            fontWeight = FontWeight.SemiBold, fontSize = 30.sp
        )


    }
}


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
                tint = Color.White
            )
            Text(
                text = stringResource(R.string.sunset),
                color = Color.LightGray,
                fontSize = 14.sp
            )
            Text(
                text = sunsetTime,
                color = Color.White,
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
            Slider(
                value = progress,
                onValueChange = { },
                valueRange = 0f..1f,
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.White,
                    inactiveTrackColor = Color.Gray
                ),
                modifier = Modifier
                    .height(40.dp)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(id = R.drawable.sunrise),
                contentDescription = "Sunrise",
                tint = Color.White
            )
            Text(
                text = stringResource(R.string.sunrise),
                color = Color.LightGray,
                fontSize = 14.sp
            )
            Text(
                text = sunriseTime,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
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
                tint = Color.White
            )
            Text(
                text = stringResource(R.string.sunrise),
                color = colorSunText,
                fontSize = 14.sp
            )
            Text(
                text = sunriseTime,
                color = Color.White,
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
            Slider(
                value = progress,
                onValueChange = { },
                valueRange = 0f..1f,
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.White,
                    inactiveTrackColor = Color.Gray
                ),
                modifier = Modifier
                    .height(3.dp)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(id = R.drawable.sunset),
                contentDescription = "Sunset",
                tint = Color.White
            )
            Text(
                text = stringResource(R.string.sunset),
                color = colorSunText,
                fontSize = 14.sp,

                )
            Text(
                text = sunsetTime,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun WeatherCard(weather: WeatherResponse) {

    val description = "Feels Like ${weather.main.feels_like.toInt()}°${Constants.degree}"
    val tempDegree = "${weather.main.temp.toInt()}"

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 8.dp)
    ) {
        val (backgroundCard, title, lastUpdate, weatherImage, degree) = createRefs()
        CardBackground(modifier = Modifier.constrainAs(backgroundCard) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
        }, height = 240, weather.weather[0].icon)

        val icon = IconsMapper.iconsMap.get(weather.weather.get(0).icon)
        icon?.let { painterResource(it) }?.let {
            Image(
                painter = it,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(175.dp)
                    .constrainAs(weatherImage) {
                        absoluteLeft.linkTo(backgroundCard.absoluteLeft)
                        top.linkTo(backgroundCard.top)
                    }
            )
        }

        Text(
            text = weather.weather[0].description,
            style = MaterialTheme.typography.titleLarge,
            color = ColorTextSecondary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.constrainAs(title) {
                start.linkTo(anchor = weatherImage.start, margin = 24.dp)
                top.linkTo(anchor = weatherImage.bottom)
            }
        )
        Text(
            text = "Last Updated: ${DateHelper.getRelativeTime(weather.dt)}",
            color = ColorTextSecondary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.constrainAs(lastUpdate) {
                start.linkTo(anchor = title.start)
                top.linkTo(anchor = title.bottom, margin = 4.dp)
            }
        )
        Column(
            modifier = Modifier.constrainAs(degree) {
                top.linkTo(anchor = weatherImage.top)
                bottom.linkTo(anchor = weatherImage.bottom)
                absoluteRight.linkTo(anchor = parent.absoluteRight, margin = 24.dp)
            }, horizontalAlignment = Alignment.End
        ) {
            Box(
                modifier = Modifier.padding(top = 20.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                Row {
                    Text(
                        text = tempDegree,
                        letterSpacing = 0.sp,
                        style = TextStyle(
                            brush = Brush.verticalGradient(
                                0f to Color.White,
                                1f to Color.White.copy(alpha = 0.3f)
                            ),
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Black
                        )
                    )
                    Text(
                        text = "°${Constants.degree}",
                        style = TextStyle(
                            brush = Brush.verticalGradient(
                                0f to Color.White,
                                1f to Color.White.copy(alpha = 0.3f)
                            ),
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Light,
                        ),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = ColorTextSecondaryVariant,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

}

@Composable
fun CardBackground(
    modifier: Modifier = Modifier, height: Int = 250,
    condition: String = "02d"
) {

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(height.dp)
            .padding(8.dp)
    ) {
        val topHeightRatio = 0.3f
        val cornerRadius = 36.dp.toPx()
        val topHeight = size.height * topHeightRatio

        val path = Path().apply {
            moveTo(0f + cornerRadius, topHeight)
            quadraticBezierTo(0f, topHeight, 0f, topHeight + cornerRadius)
            lineTo(0f, size.height - cornerRadius)
            quadraticBezierTo(0f, size.height, cornerRadius, size.height)
            lineTo(size.width - cornerRadius, size.height)
            quadraticBezierTo(size.width, size.height, size.width, size.height - cornerRadius)
            lineTo(size.width, size.height * 0.1f + cornerRadius)
            quadraticBezierTo(
                size.width,
                size.height * 0.1f,
                size.width - cornerRadius,
                size.height * 0.1f
            )
            lineTo(cornerRadius, topHeight)
            close()
        }

        drawPath(
            path = path,
            brush = BackgroundMapper.getCardBackground(condition)
            /*brush = Brush.linearGradient(
                0f to ColorGradient1,
                0.5f to ColorGradient2,
                1f to ColorGradient3
            )*/,
            style = Fill
        )
    }
}

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
    hour : ForecastResponse.Item0
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "${DateHelper.getHourFormTime(hour.dt.toLong())}",
            color = Color.Gray,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(end = 16.dp)
        )
        Image(
            painter = painterResource(IconsMapper.iconsMap.get(hour.weather.get(0).icon)?:R.drawable.clear_sky_day),
            contentDescription = "Sunset",
            modifier = Modifier.size(48.dp)
        )
        Text(text = "${hour.main.temp.toInt()}°${Constants.degree}", color = Color.DarkGray, fontSize = 14.sp, fontWeight = FontWeight.Bold)

    }

}


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

    val date = DateHelper.getDayFormTimestamp(daylist.get(0).dt.toLong())
    val icon = daylist.first().weather.get(0).icon.replace("n","d")

    var min = daylist.get(0).main.temp_min.toInt()
    var max = daylist.get(0).main.temp_max.toInt()


    for (item in daylist){
        min = Math.min(item.main.temp_min.toInt() , min)
        max = Math.max(item.main.temp_max.toInt() , max)
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
                text = "${date}",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

        }

        Image(
            painter = painterResource(id = IconsMapper.iconsMap.get(icon) ?: R.drawable.clear_sky_day),
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


        Text(text = "${min}°${Constants.degree} / ${max}°${Constants.degree}",
            color = Color.White,
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

@Composable
fun WindDirectionIndicator(modifier: Modifier, windDirection: Float = 299f) {
    Canvas(
        modifier = modifier
            .size(86.dp)
            .padding(16.dp)
    ) {
        val radius = size.minDimension / 2
        val center = Offset(size.width / 2, size.height / 2)

        // Draw the circle markers
        for (i in 0 until 12) {
            val angle = Math.toRadians((i * 30).toDouble())
            val start = Offset(
                center.x + (radius - 10) * cos(angle).toFloat(),
                center.y + (radius - 10) * sin(angle).toFloat()
            )
            val end = Offset(
                center.x + radius * cos(angle).toFloat(),
                center.y + radius * sin(angle).toFloat()
            )
            drawLine(
                color = Color.White,
                start = start,
                end = end,
                strokeWidth = 6f
            )
        }

        // Draw the arrow
        val arrowAngle = Math.toRadians(windDirection.toDouble())
        val arrowStart = Offset(
            center.x + (radius - 20) * cos(arrowAngle).toFloat(),
            center.y + (radius - 20) * sin(arrowAngle).toFloat()
        )
        drawLine(
            color = Color.Red,
            start = center,
            end = arrowStart,
            strokeWidth = 4f
        )

        // Draw the arrowhead
        val arrowHeadSize = 10f
        val arrowTip = Offset(
            center.x + radius * cos(arrowAngle).toFloat(),
            center.y + radius * sin(arrowAngle).toFloat()
        )
        drawCircle(
            color = Color.Red,
            radius = arrowHeadSize,
            center = arrowTip
        )
    }
}

@Composable
fun CardItem(icon: Int
             , title: String = "dddd", value: String = "455"
             ,brush:Brush
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
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = title,
                color = Color.White,
                fontSize = 14.sp
            )
            Text(
                text = value,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}

@Preview
@Composable
fun WeatherGrid(
    brush: Brush = BackgroundMapper.getCardBackground("01d")
    ,pressure : String = "100" , humidity:String = "20" ,visibility : String = "1000"
) {
    val items = listOf(
        Triple(R.drawable.humidity_icon, stringResource(R.string.humidity), humidity),
        Triple(R.drawable.air_icon, stringResource(R.string.air_pressure), pressure),
        Triple(R.drawable.outline_visibility_24, "Visibility", visibility)
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
            CardItem(icon = icon, title = title, value = value , brush)
        }
    }
}

@Preview
@Composable
fun WindCard(value: Double = 445.0 , degree:Float = 270f
             ,brush:Brush = BackgroundMapper.getCardBackground("01d")
) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(brush = brush, shape = RoundedCornerShape(12.dp))

        ) {
            val (directions , des , windIcon , windTitle) = createRefs()

            Icon(
                painter = painterResource(id = R.drawable.wind_icon),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(32.dp)
                    .constrainAs(windIcon) {
                        start.linkTo(parent.start, 16.dp)
                        top.linkTo(parent.top, 16.dp)
                    }
            )

            Text(
                text = stringResource(R.string.winds_speed_directions),
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.constrainAs(windTitle){
                    start.linkTo(windIcon.end,8.dp)
                    top.linkTo(windIcon.top)
                    bottom.linkTo(windIcon.bottom)
                }
            )

            Text(
                text = "$value ${Constants.windUnit}",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.constrainAs(des){
                    top.linkTo(windIcon.bottom)
                    start.linkTo(windIcon.start)
                    end.linkTo(windTitle.end)
                    bottom.linkTo(directions.bottom)
                }
            )
            WindDirectionIndicator(Modifier.constrainAs(directions){
                end.linkTo(parent.end , 16.dp)

            } ,degree)

    }
}





















