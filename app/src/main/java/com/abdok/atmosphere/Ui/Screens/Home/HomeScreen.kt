package com.abdok.atmosphere.Ui.Screens.Home

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.abdok.atmosphere.Data.DataSources.RemoteDataSource
import com.abdok.atmosphere.Data.Remote.RetroConnection
import com.abdok.atmosphere.Repository

import androidx.lifecycle.viewmodel.compose.*
import com.abdok.atmosphere.Data.Models.WeatherResponse
import com.abdok.atmosphere.R
import com.abdok.atmosphere.Ui.theme.ColorBackground
import com.abdok.atmosphere.Ui.theme.ColorGradient1
import com.abdok.atmosphere.Ui.theme.ColorGradient2
import com.abdok.atmosphere.Ui.theme.ColorGradient3
import com.abdok.atmosphere.Ui.theme.ColorTextSecondary
import com.abdok.atmosphere.Ui.theme.ColorTextSecondaryVariant
import com.abdok.atmosphere.Utils.Constants
import com.abdok.atmosphere.Utils.CountryHelper
import com.abdok.atmosphere.Utils.DateHelper
import com.abdok.atmosphere.Utils.IconsMapper

@Composable
fun HomeScreen(){

    val homeFactory = HomeViewModelFactory(Repository.getInstance(
        RemoteDataSource.getInstance(
            RetroConnection.retroServices)))

    val viewModel: HomeViewModel = viewModel(factory = homeFactory)


    val loc1 = 30.666733 to 31.169271
    val loc2 = 50.666733 to 8.468946
    val loc3 = 4.666733 to 8.468946
    val loc4 = 60.666733 to 11.169271
    val loc5 = 4.666733 to 36.169271

    val  loc = loc1


    viewModel.getWeatherAndForecastLatLon(loc.first, loc.second)

    val combinedWeatherData = viewModel.combinedWeatherData.observeAsState()
    val messageState = viewModel.error.observeAsState()



    Log.d("TAG", "HomeScreen weather: ${combinedWeatherData.value?.weatherResponse.toString()}")
    Log.d("TAG", "HomeScreen Forecast: ${combinedWeatherData.value?.forecastResponse.toString()}")

    messageState.value.let {
        Log.e("TAG", "HomeScreen error: $it")
    }

    Box(modifier = Modifier
        .padding(horizontal = 16.dp)
        .fillMaxSize()){
        combinedWeatherData.value?.weatherResponse?.let {
            //DailyWeather(it)
            Column {
                topView(it.name , CountryHelper.getCountryNameFromCode(it.sys.country)?:"")
                Spacer(modifier = Modifier.height(8.dp))
                WeatherCard(it)
            }

        }

    }
}

@Preview
@Composable
fun topView(
    city : String = "Zefta",
    country : String = "Egypt"
){

    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 32.dp, start = 16.dp)) {

        Text(text = "$city," ,
            fontWeight = FontWeight.SemiBold
            ,fontSize = 30.sp)

        Text(text = country ,
            fontWeight = FontWeight.SemiBold
            ,fontSize = 30.sp)





    }
}


/*@Composable
fun DailyWeather(weather: WeatherResponse) {
    ConstraintLayout(
        modifier = Modifier.fillMaxWidth()
    ) {
        val (forecastImage, forecastValue, windImage, title, description, background) = createRefs()

        CardBackground(
            modifier = Modifier.constrainAs(background) {
                linkTo(
                    start = parent.start,
                    end = parent.end,
                    top = parent.top,
                    bottom = description.bottom,
                    topMargin = 48.dp
                )
                height = Dimension.fillToConstraints
            }
        )

        val icon = IconsMapper.iconsMap.get(weather.weather.get(0).icon)
        icon?.let { painterResource(it) }?.let {
            Image(
                painter = it,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(175.dp)
                    .constrainAs(forecastImage) {
                        start.linkTo(anchor = parent.start, margin = 4.dp)
                        top.linkTo(parent.top)
                    }
            )
        }

        Text(
            text = weather.weather[0].description,
            style = MaterialTheme.typography.titleLarge,
            color = ColorTextSecondary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.constrainAs(title) {
                start.linkTo(anchor = parent.start, margin = 24.dp)
                top.linkTo(anchor = forecastImage.bottom)
            }
        )

        Text(
            text = "Last Updated: ${DateHelper.getRelativeTime(weather.dt)}",
            style = MaterialTheme.typography.bodyMedium,
            color = ColorTextSecondaryVariant,
            modifier = Modifier
                .constrainAs(description) {
                    start.linkTo(anchor = title.start)
                    top.linkTo(anchor = title.bottom)
                }
                .padding(bottom = 24.dp)
        )

        ForecastValue(
            modifier = Modifier.constrainAs(forecastValue) {
                end.linkTo(anchor = parent.end, margin = 24.dp)
                top.linkTo(forecastImage.top)
                bottom.linkTo(forecastImage.bottom)

            },description = "Feels Like ${weather.main.feels_like}°${Constants.degree}"
            , degree = "${weather.main.temp.toInt()}"
        )
    }
}*/

/*
@Composable
private fun CardBackground(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    0f to ColorGradient1,
                    0.5f to ColorGradient2,
                    1f to ColorGradient3
                ),
                shape = RoundedCornerShape(32.dp)
            )
    )
}
*/

/*@Composable
private fun ForecastValue(
    modifier: Modifier = Modifier,
    degree: String,
    description: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        Box(modifier = Modifier.padding(top = 20.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Row {
                Text(
                    text = degree,
                    letterSpacing = 0.sp,
                    style = TextStyle(
                        brush = Brush.verticalGradient(
                            0f to Color.White,
                            1f to Color.White.copy(alpha = 0.3f)
                        ),
                        fontSize = 40.sp,
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
                    *//*modifier = Modifier.padding(top = 2.dp)*//*
                )
            }
        }
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = ColorTextSecondaryVariant
        )
    }
}*/



@Composable
fun WeatherCard(weather: WeatherResponse) {

    val description = "Feels Like ${weather.main.feels_like.toInt()}°${Constants.degree}"
    val tempDegree = "${weather.main.temp.toInt()}"

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        val (backgroundCard, title, lastUpdate , weatherImage , degree) = createRefs()
        CardBackground(modifier = Modifier.constrainAs(backgroundCard) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
        }, height = 240)

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
                start.linkTo(anchor = weatherImage.start)
                end.linkTo(anchor = weatherImage.end)
                top.linkTo(anchor = weatherImage.bottom)
            }
        )
        Text(
            text = "Last Updated: ${DateHelper.getRelativeTime(weather.dt)}",
            color = ColorTextSecondary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.constrainAs(lastUpdate) {
                start.linkTo(anchor = title.start)
                top.linkTo(anchor = title.bottom , margin = 4.dp)
            }
        )
        Column(
            modifier = Modifier.constrainAs(degree) {
                top.linkTo(anchor = weatherImage.top)
                bottom.linkTo(anchor = weatherImage.bottom)
                absoluteRight.linkTo(anchor = parent.absoluteRight , margin = 24.dp)
            }
            ,horizontalAlignment = Alignment.End
        ) {
            Box(modifier = Modifier.padding(top = 20.dp),
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
    modifier: Modifier = Modifier, height : Int = 250
) {

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(height.dp)
            .padding(8.dp)
    ) {
        val topHeightRatio = 0.3f
        val cornerRadius =36.dp.toPx()
        val topHeight = size.height * topHeightRatio

        val path = Path().apply {
            moveTo(0f + cornerRadius, topHeight)
            quadraticBezierTo(0f, topHeight, 0f, topHeight + cornerRadius)
            lineTo(0f, size.height - cornerRadius)
            quadraticBezierTo(0f, size.height, cornerRadius, size.height)
            lineTo(size.width - cornerRadius, size.height)
            quadraticBezierTo(size.width, size.height, size.width, size.height - cornerRadius)
            lineTo(size.width, size.height * 0.1f + cornerRadius)
            quadraticBezierTo(size.width, size.height * 0.1f, size.width - cornerRadius, size.height * 0.1f)
            lineTo(cornerRadius, topHeight)
            close()
        }

        drawPath(
            path = path,
            brush = Brush.linearGradient(
                0f to ColorGradient1,
                0.5f to ColorGradient2,
                1f to ColorGradient3
            ),
            style = Fill
        )
    }
}

/*

@Preview
@Composable
private fun WindForecastImage(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(),
            contentDescription = null,
            modifier = Modifier.size(60.dp),
            tint = ColorWindForecast
        )
        Icon(
            painter = painterResource(R.drawable),
            contentDescription = null,
            modifier = Modifier.size(60.dp),
            tint = ColorWindForecast
        )
    }
}
*/



