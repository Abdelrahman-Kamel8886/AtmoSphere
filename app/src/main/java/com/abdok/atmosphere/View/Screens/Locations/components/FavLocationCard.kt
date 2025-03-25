package com.abdok.atmosphere.View.Screens.Locations.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.abdok.atmosphere.Data.Models.FavouriteLocation
import com.abdok.atmosphere.R
import com.abdok.atmosphere.Utils.Constants
import com.abdok.atmosphere.Utils.CountryHelper
import com.abdok.atmosphere.Utils.Dates.DateHelper
import com.abdok.atmosphere.Utils.ViewHelpers.BackgroundMapper
import com.abdok.atmosphere.Utils.ViewHelpers.IconsMapper
import kotlinx.coroutines.delay


@Composable
fun FavouriteLocationCard(item: FavouriteLocation) {
    val weatherResponse = item.combinedWeatherData.weatherResponse

    val emojii = CountryHelper.getFlagEmoji(weatherResponse.sys.country)
    val name = "${item.name.replace(", ", "\n")}"
//    val name = item.name

    val condition = weatherResponse.weather.get(0).icon
    val brush = BackgroundMapper.getCardBackground(condition)
    val icon = IconsMapper.iconsMap.get(condition) ?: R.drawable.sun

    val tempDegree = weatherResponse.main.temp_min.toInt() to weatherResponse.main.temp_max.toInt()
    val conditionText = weatherResponse.weather.get(0).description

    val context = LocalContext.current

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(brush = brush, shape = RoundedCornerShape(16.dp))

    ) {
        val (directions, des, date, windIcon, windTitle) = createRefs()

/*        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(32.dp)
                .constrainAs(windIcon) {
                    start.linkTo(parent.start, 16.dp)
                    top.linkTo(parent.top, 16.dp)
                }
        )*/
        Text(
            text = emojii,
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .size(32.dp)
                .constrainAs(windIcon) {
                    start.linkTo(parent.start, 16.dp)
                    top.linkTo(parent.top, 16.dp)
                }
        )

       Text(
            text = name,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(windTitle) {
                start.linkTo(windIcon.end, 8.dp)
                top.linkTo(windIcon.top)
                bottom.linkTo(windIcon.bottom)
            }
        )
/*
        Text(
            text = "$conditionText\n${tempDegree.first}°${Constants.degree} / ${tempDegree.second}°${Constants.degree}",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            modifier = Modifier.constrainAs(des) {
                top.linkTo(windIcon.bottom, 16.dp)
                start.linkTo(windIcon.start, 8.dp)
            }
        )*/

        Text(
            text = "${stringResource(id = R.string.last_updated)} ${DateHelper.getRelativeTime(weatherResponse.dt, context)}",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            modifier = Modifier.constrainAs(date) {
                top.linkTo(windIcon.bottom)
                start.linkTo(windIcon.start, 8.dp)
                bottom.linkTo(parent.bottom)
            }
        )
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .constrainAs(directions) {
                    end.linkTo(parent.end, 16.dp)
                }
        )

    }
}


@Composable
fun <T> SwipeToDeleteContainer(
    item: T,
    onDelete: (T) -> Unit,
    onRestore: (T) -> Unit = {},
    animationDuration: Int = 500,
    snackbarHostState:SnackbarHostState,
    content: @Composable (T) -> Unit
) {

    val context = LocalContext.current

    var isRemoved by remember { mutableStateOf(false) }
    var canSwipe by remember { mutableStateOf(true) }

    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                isRemoved = true
                canSwipe = false
                true
            } else {
                false
            }
        }
    )
    LaunchedEffect(isRemoved) {
        if (isRemoved) {
            val result = snackbarHostState.showSnackbar(
                message = context.getString(R.string.location_deleted_successfully),
                actionLabel = context.getString(R.string.undo),
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                onRestore(item)
                canSwipe = true
                isRemoved = false
            } else {
//                delay(animationDuration.toLong())
                onDelete(item)
            }
        }
    }



    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            shrinkTowards = Alignment.Top,
            animationSpec = tween(durationMillis = animationDuration)
        ) + fadeOut()
    ) {
        if (canSwipe){
            SwipeToDismissBox(
                state = state,
                backgroundContent = { DeleteBackground(state) },
                enableDismissFromStartToEnd = false
            ) {
                content(item)
            }
        }else{
            LaunchedEffect(Unit) {
                state.snapTo(SwipeToDismissBoxValue.Settled)
            }
            content(item)
        }
    }


}

@Composable
fun DeleteBackground(
    swipeDismissState: SwipeToDismissBoxState
) {
    val color = if (swipeDismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
        Color.Red
    } else {
        Color.Transparent
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp)
            .padding(start = 24.dp)
            .background(color)
                ,
        contentAlignment = Alignment.CenterEnd
    ) {

        Icon(
            modifier = Modifier.padding(16.dp),
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = Color.White,
        )
    }

}



























