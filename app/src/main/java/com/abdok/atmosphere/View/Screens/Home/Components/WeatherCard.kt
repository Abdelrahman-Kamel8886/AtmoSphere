package com.abdok.atmosphere.View.Screens.Home.Components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.abdok.atmosphere.Data.Models.WeatherResponse
import com.abdok.atmosphere.R
import com.abdok.atmosphere.Utils.Dates.DateHelper
import com.abdok.atmosphere.Utils.LanguageManager
import com.abdok.atmosphere.Utils.SharedModel
import com.abdok.atmosphere.Utils.ViewHelpers.BackgroundMapper
import com.abdok.atmosphere.Utils.ViewHelpers.IconsMapper
import com.abdok.atmosphere.View.theme.ColorTextSecondary

@Composable
fun WeatherCard(weather: WeatherResponse) {

    val description =
        "${stringResource(R.string.feels_like)} ${weather.main.feels_like.toInt()} ${SharedModel.currentDegree}"
    val tempDegree = "${weather.main.temp.toInt()}"
    val context = LocalContext.current

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

            text = "${stringResource(R.string.last_updated)} ${
                DateHelper.getRelativeTime(
                    weather.dt,
                    context
                )
            }",
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
                        text = LanguageManager.formatNumberBasedOnLanguage(tempDegree),
                        letterSpacing = 0.sp,
                        style = TextStyle(
                            brush = Brush.verticalGradient(
                                0f to ColorTextSecondary,
                                1f to ColorTextSecondary.copy(alpha = 0.3f)
                            ),
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Black
                        )
                    )
                    Text(
                        text = " ${SharedModel.currentDegree}",
                        style = TextStyle(
                            brush = Brush.verticalGradient(
                                0f to ColorTextSecondary,
                                1f to ColorTextSecondary.copy(alpha = 0.3f)
                            ),
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Light,
                        ),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
            Text(
                text = LanguageManager.formatNumberBasedOnLanguage(description),
                color = ColorTextSecondary.copy(alpha = 0.7f),
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
