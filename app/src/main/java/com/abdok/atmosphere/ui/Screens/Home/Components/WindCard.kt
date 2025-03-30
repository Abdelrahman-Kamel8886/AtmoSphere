package com.abdok.atmosphere.ui.Screens.Home.Components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.abdok.atmosphere.R
import com.abdok.atmosphere.Utils.LanguageManager
import com.abdok.atmosphere.Utils.SharedModel
import com.abdok.atmosphere.Utils.ViewHelpers.BackgroundMapper
import com.abdok.atmosphere.ui.Theme.ColorTextSecondary
import kotlin.math.cos
import kotlin.math.sin

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
                color = ColorTextSecondary,
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


@Preview
@Composable
fun WindCard(
    value: Double = 445.0,
    degree: Float = 270f,
    brush: Brush = BackgroundMapper.getCardBackground("01d")
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(brush = brush, shape = RoundedCornerShape(12.dp))

    ) {
        val (directions, des, windIcon, windTitle) = createRefs()

        Icon(
            painter = painterResource(id = R.drawable.wind_icon),
            contentDescription = null,
            tint = ColorTextSecondary,
            modifier = Modifier
                .size(32.dp)
                .constrainAs(windIcon) {
                    start.linkTo(parent.start, 16.dp)
                    top.linkTo(parent.top, 16.dp)
                }
        )

        Text(
            text = stringResource(R.string.winds_speed_directions),
            color = ColorTextSecondary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(windTitle) {
                start.linkTo(windIcon.end, 8.dp)
                top.linkTo(windIcon.top)
                bottom.linkTo(windIcon.bottom)
            }
        )
        val speed = "$value ${SharedModel.currentSpeed}"
        Text(
            text = LanguageManager.formatNumberBasedOnLanguage(speed),
            color = ColorTextSecondary,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.constrainAs(des) {
                top.linkTo(windIcon.bottom)
                start.linkTo(windIcon.start)
                end.linkTo(windTitle.end)
                bottom.linkTo(directions.bottom)
            }
        )
        WindDirectionIndicator(Modifier.constrainAs(directions) {
            end.linkTo(parent.end, 16.dp)

        }, degree)

    }
}