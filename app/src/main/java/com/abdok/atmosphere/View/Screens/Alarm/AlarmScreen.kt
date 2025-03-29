package com.abdok.atmosphere.View.Screens.Alarm

import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import com.abdok.atmosphere.FloatingWindowService
import com.abdok.atmosphere.R
import com.abdok.atmosphere.Utils.setAlarm
import com.abdok.atmosphere.View.Screens.Alarm.Components.AlarmBottomSheet
import com.abdok.atmosphere.View.Screens.Alarm.Components.EmptyAlarmsView

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun AlertsScreen() {

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isSheetOpen by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
//                    isSheetOpen = true
                    context.setAlarm(10)
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
        EmptyAlarmsView(padding = innerPadding)

        Box(
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
        }
        if (isSheetOpen) {
            ModalBottomSheet(
                onDismissRequest = { isSheetOpen = false },
                sheetState = sheetState, containerColor = Color.White
            )
            {
                AlarmBottomSheet { isSheetOpen = false }
            }
        }
    }
}

