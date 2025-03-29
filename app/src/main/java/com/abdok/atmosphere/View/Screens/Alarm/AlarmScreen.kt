package com.abdok.atmosphere.View.Screens.Alarm

import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abdok.atmosphere.Data.Models.AlertDTO
import com.abdok.atmosphere.Data.Models.FavouriteLocation.Companion.toJson
import com.abdok.atmosphere.Data.Response
import com.abdok.atmosphere.FloatingWindowService
import com.abdok.atmosphere.R
import com.abdok.atmosphere.Utils.convertArabicToEnglish
import com.abdok.atmosphere.Utils.durationFromNowInSeconds
import com.abdok.atmosphere.Utils.setAlarm
import com.abdok.atmosphere.View.Screens.Alarm.Components.AlarmBottomSheet
import com.abdok.atmosphere.View.Screens.Alarm.Components.AlertsListView
import com.abdok.atmosphere.View.Screens.Alarm.Components.EmptyAlarmsView
import com.abdok.atmosphere.View.Screens.Locations.Components.FavouriteLocationsView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertsScreen(viewModel: AlarmViewModel) {

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isSheetOpen by remember { mutableStateOf(false) }

    val alerts = viewModel.alerts.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getAlerts()
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                snackbarHostState,
                modifier = Modifier.wrapContentHeight(align = Alignment.Top)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    isSheetOpen = true
                    //context.setAlarm(10)
                }, containerColor = Color.DarkGray,
                modifier = Modifier.padding(bottom = 56.dp), shape = RoundedCornerShape(100.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_add_alert_24),
                    tint = Color.White,
                    contentDescription = "add"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { innerPadding ->

        when (alerts.value) {
            is Response.Error -> {
                EmptyAlarmsView(padding = innerPadding)
            }

            Response.Loading -> {
                CircularProgressIndicator()
            }

            is Response.Success -> {

                val data = (alerts.value as Response.Success).data
                var list by remember { mutableStateOf(data) }

                if (data.isEmpty()) {
                    EmptyAlarmsView(padding = innerPadding)
                } else {
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                    ) {

                        AlertsListView(
                            alerts = list,
                            snackbarHostState = snackbarHostState
                        ){
                            viewModel.deleteAlert(it)
                            list -= it
                        }
                    }
                }
            }
        }


        /*Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
            , contentAlignment = Alignment.Center
        ){
            Button(onClick = {
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(context, FloatingWindowService::class.java)
                    context.startService(intent)
                }, 5000)
            }) {
                Text("Start Floating Window")
            }
        }*/
        if (isSheetOpen) {
            ModalBottomSheet(
                onDismissRequest = { isSheetOpen = false },
                sheetState = sheetState, containerColor = Color.White
            )
            {
                AlarmBottomSheet(
                    onClose = { isSheetOpen = false }
                )
                { startDuration, endDuration, selectedOption ->
                    val alert = AlertDTO(
                        startDuration = startDuration.convertArabicToEnglish(),
                        endDuration = endDuration.convertArabicToEnglish(),
                        selectedOption = selectedOption
                    )
                    val duration = startDuration.durationFromNowInSeconds()
                    Log.i("TAG", "Alert Scheduled within : $duration seconds")
                    viewModel.addAlert(alert)
                    viewModel.getAlerts()
                    context.setAlarm(duration)
                    isSheetOpen = false
                }
            }
        }
    }
}

