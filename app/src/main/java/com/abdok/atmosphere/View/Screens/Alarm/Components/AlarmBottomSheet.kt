package com.abdok.atmosphere.View.Screens.Alarm.Components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun AlarmBottomSheet(onClose: () -> Unit) {

    val redColor = Color(0xFFE53935)
    val greenColor = Color(0xFF43A047)

    val context = LocalContext.current

    // State to track the selected option
    var selectedOption by remember { mutableStateOf("Alarm") }
    var startDurationTimeState by remember { mutableStateOf("") }
    var endDurationTimeState by remember { mutableStateOf("") }


    val startDurationSource = remember {
        object : MutableInteractionSource {
            override val interactions = MutableSharedFlow<Interaction>(
                extraBufferCapacity = 16,
                onBufferOverflow = BufferOverflow.DROP_OLDEST,
            )
            override suspend fun emit(interaction: Interaction) {
                if (interaction is PressInteraction.Release) {
                    endDurationTimeState = "14:00 AM"
                }
                interactions.emit(interaction)
            }
            override fun tryEmit(interaction: Interaction): Boolean {
                return interactions.tryEmit(interaction)
            }
        }
    }

    val endDurationSource = remember {
        object : MutableInteractionSource {
            override val interactions = MutableSharedFlow<Interaction>(
                extraBufferCapacity = 16,
                onBufferOverflow = BufferOverflow.DROP_OLDEST,
            )
            override suspend fun emit(interaction: Interaction) {
                if (interaction is PressInteraction.Release) {
                    startDurationTimeState = "14:00 AM"
                }
                interactions.emit(interaction)
            }
            override fun tryEmit(interaction: Interaction): Boolean {
                return interactions.tryEmit(interaction)
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Add New Alert",
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))




        // 🕑 Start Duration
        Text(text = "Start duration", style = MaterialTheme.typography.bodyMedium)
        OutlinedTextField(
            value = startDurationTimeState,
            onValueChange = {},
            readOnly = true,
            placeholder = { Text("Start duration") },
            label = {Text("Start duration")},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "Start Time"
                )
            },
            interactionSource = startDurationSource ,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ⏰ End Duration
        Text(text = "End duration", style = MaterialTheme.typography.bodyMedium)
        OutlinedTextField(
            value = endDurationTimeState,
            onValueChange = { },
            placeholder = { Text("End duration") },
            label = {Text("End duration")},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Timer,
                    contentDescription = "End Time"
                )
            },
            readOnly = true, // Make it read-only so the click event is not consumed internally
            interactionSource = endDurationSource,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Radio Buttons
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Notify me by",
                style = MaterialTheme.typography.bodyLarge
            )

            Row {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { selectedOption = "Alarm" }
                ) {
                    RadioButton(
                        selected = selectedOption == "Alarm",
                        onClick = { selectedOption = "Alarm" }
                    )
                    Text(text = "Alarm", modifier = Modifier.padding(start = 4.dp))
                }

                Spacer(modifier = Modifier.width(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { selectedOption = "Notification" }
                ) {
                    RadioButton(
                        selected = selectedOption == "Notification",
                        onClick = { selectedOption = "Notification" }
                    )
                    Text(text = "Notification", modifier = Modifier.padding(start = 4.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.weight(0.6f))

            Button(
                onClick = { onClose()},
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(greenColor),
                modifier = Modifier.weight(3.2f)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Save"
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Save")
            }

            Spacer(modifier = Modifier.weight(0.6f))

            Button(
                onClick = onClose,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(redColor),
                modifier = Modifier.weight(3.2f)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close"
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Cancel")
            }

            Spacer(modifier = Modifier.weight(0.6f))
        }
    }
}


@Preview
@Composable
fun PreviewAlarmBottomSheet() {
    AlarmBottomSheet({})
}

