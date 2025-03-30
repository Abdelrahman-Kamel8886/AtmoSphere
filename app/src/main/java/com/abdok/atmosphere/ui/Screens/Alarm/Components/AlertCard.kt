package com.abdok.atmosphere.ui.Screens.Alarm.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abdok.atmosphere.data.models.AlertDTO
import com.abdok.atmosphere.enums.Alert
import com.abdok.atmosphere.R
import com.abdok.atmosphere.utils.viewHelpers.BackgroundMapper
import com.abdok.atmosphere.utils.convertNumbersBasedOnLanguage

@Composable
fun AlertCard(item: AlertDTO) {

    val startDuration  = item.startDuration
    val endDuration = item.endDuration

    val brush = BackgroundMapper.getScreenBackground("50n")

    val icon = if(item.selectedOption == Alert.ALARM) Icons.Default.Alarm else Icons.Default.Notifications


    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp , vertical = 8.dp)
        .background(brush, RoundedCornerShape(8.dp))
        .padding(16.dp)
        , verticalAlignment = Alignment.CenterVertically
        , horizontalArrangement = Arrangement.spacedBy(16.dp)

    ){
        Icon(
            imageVector = icon,
            tint = Color.White,
            contentDescription = null
        )
        Text(text = "$startDuration - $endDuration".convertNumbersBasedOnLanguage() ,
            color = Color.White
            , fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium
        )
    }


}


@Preview(showBackground = true , showSystemUi = true)
@Composable
fun previewAlertList(){
    LazyColumn {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    tint = Color.DarkGray,
                    contentDescription = null
                )
                Text(
                    text = stringResource(R.string.scheduled_alerts),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
            }
        }
        items(5){
            AlertCard(AlertDTO(10,"20","30", Alert.ALARM))
        }
    }
}
