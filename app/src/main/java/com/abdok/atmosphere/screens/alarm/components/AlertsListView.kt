package com.abdok.atmosphere.screens.alarm.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.abdok.atmosphere.R
import com.abdok.atmosphere.data.models.AlertDTO
import com.abdok.atmosphere.screens.Locations.components.SwipeToDeleteContainer


@Composable
fun AlertsListView(
    alerts: List<AlertDTO>,
    snackbarHostState: SnackbarHostState,
    onDelete: (AlertDTO) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp)
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
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(alerts , key = {it.id}){
                    SwipeToDeleteContainer(
                        item = it,
                        onDelete = { onDelete(it) },
                        snackbarHostState = snackbarHostState,

                    ) {
                        AlertCard(it)
                    }
            }
        }

        Spacer(modifier = Modifier.height(150.dp))
    }
}
