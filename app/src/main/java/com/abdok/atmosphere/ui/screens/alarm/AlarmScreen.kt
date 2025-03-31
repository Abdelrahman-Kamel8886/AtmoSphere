package com.abdok.atmosphere.ui.screens.alarm

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abdok.atmosphere.data.models.AlertDTO
import com.abdok.atmosphere.data.Response
import com.abdok.atmosphere.R
import com.abdok.atmosphere.ui.CurvedNavBar
import com.abdok.atmosphere.utils.cancelAlarm
import com.abdok.atmosphere.utils.convertArabicToEnglish
import com.abdok.atmosphere.utils.durationFromNowInSeconds
import com.abdok.atmosphere.utils.setAlarm
import com.abdok.atmosphere.ui.screens.alarm.components.AlarmBottomSheet
import com.abdok.atmosphere.ui.screens.alarm.components.AlertsListView
import com.abdok.atmosphere.ui.screens.alarm.components.EmptyAlarmsView
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertsScreen(viewModel: AlarmViewModel) {

    LaunchedEffect(Unit) {
        CurvedNavBar.mutableNavBarState.emit(true)
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isSheetOpen by remember { mutableStateOf(false) }

    val alerts = viewModel.alerts.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    val permission = Manifest.permission.POST_NOTIFICATIONS
    val permissionState = remember { mutableStateOf(isPermissionGranted(context, permission)) }
    val permanentlyDenied = remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        permissionState.value = isGranted
        if (isGranted) {
            isSheetOpen = true
        } else {
            permanentlyDenied.value = !shouldShowRequestPermissionRationale(context, permission)
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = context.getString(R.string.permission_denied),
                )
            }
        }
    }

    LaunchedEffect(Unit) { viewModel.getAlerts() }

    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = true
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    when {
                        permissionState.value -> isSheetOpen = true
                        permanentlyDenied.value -> openAppSettings(context)
                        else -> launcher.launch(permission)
                    }
                },
                containerColor = Color.DarkGray,
                modifier = Modifier.padding(bottom = 56.dp),
                shape = RoundedCornerShape(100.dp)
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
            is Response.Error -> EmptyAlarmsView(padding = innerPadding)
            Response.Loading -> CircularProgressIndicator()
            is Response.Success -> {
                val data = (alerts.value as Response.Success).data
                var list by remember { mutableStateOf(data) }

                if (data.isEmpty()) {
                    EmptyAlarmsView(padding = innerPadding)
                } else {
                    Box(modifier = Modifier.padding(innerPadding)) {
                        AlertsListView(alerts = list, snackbarHostState = snackbarHostState) {
                            viewModel.deleteAlert(it)
                            list -= it
                            context.cancelAlarm(it.id)
                        }
                    }
                }
            }
        }

        if (isSheetOpen) {
            ModalBottomSheet(
                onDismissRequest = { isSheetOpen = false },
                sheetState = sheetState, containerColor = Color.White
            ) {
                AlarmBottomSheet(onClose = { isSheetOpen = false }) { startDuration, endDuration, selectedOption ->
                    val id = System.currentTimeMillis().toInt()
                    val alert = AlertDTO(
                        id = id,
                        startDuration = startDuration.convertArabicToEnglish(),
                        endDuration = endDuration.convertArabicToEnglish(),
                        selectedOption = selectedOption
                    )
                    val duration = startDuration.durationFromNowInSeconds()
                    Log.i("TAG", "Alert Scheduled within : $duration seconds")
                    viewModel.addAlert(alert)
                    viewModel.getAlerts()
                    context.setAlarm(duration, id)
                    isSheetOpen = false
                }
            }
        }
    }
}

private fun isPermissionGranted(context: Context, permission: String): Boolean {
    return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}

private fun shouldShowRequestPermissionRationale(context: Context, permission: String): Boolean {
    return ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission)
}

private fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
    }
    context.startActivity(intent)
}
