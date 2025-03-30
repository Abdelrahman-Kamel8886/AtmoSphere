package com.abdok.atmosphere.ui.Screens.Alarm.Components

import android.app.TimePickerDialog
import android.content.Context
import android.widget.TimePicker
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abdok.atmosphere.Enums.Alert
import com.abdok.atmosphere.Enums.AlertErrors
import com.abdok.atmosphere.R
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.Calendar

@Composable
fun AlarmBottomSheet(
    onClose: () -> Unit,
    onSaved: (
        startDuration: String,
        endDuration: String,
        selectedOption: Alert
    ) -> Unit
) {

    val redColor = Color(0xFFE53935)
    val greenColor = Color(0xFF43A047)

    val context = LocalContext.current

    var selectedOption by remember { mutableStateOf(Alert.ALARM.getLocalizedValue()) }
    var startDurationTimeState by remember { mutableStateOf("") }
    var endDurationTimeState by remember { mutableStateOf("") }
    var errorState by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()


    val startDurationSource = remember {
        object : MutableInteractionSource {
            override val interactions = MutableSharedFlow<Interaction>(
                extraBufferCapacity = 16,
                onBufferOverflow = BufferOverflow.DROP_OLDEST,
            )

            override suspend fun emit(interaction: Interaction) {
                if (interaction is PressInteraction.Release) {
                    showTimePicker(
                        context,
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE)
                    ) {
                        startDurationTimeState = it
                        errorState = ""
                    }
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

                    if (startDurationTimeState.isNotEmpty()) {
                        val parts = startDurationTimeState
                            .split(":")
                            .map { it.toInt() }
                        val hour = parts[0]
                        val minute = parts[1]

                        showTimePicker(
                            context,
                            hour,
                            minute
                        ) { time ->
                            endDurationTimeState = time
                            errorState = ""
                        }
                    } else {
                        errorState = context.getString(R.string.select_start_time_first)
                    }
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
            text = stringResource(R.string.add_new_alert),
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))


        // Start Duration
        Text(text = stringResource(R.string.start_duration), style = MaterialTheme.typography.bodyMedium)
        OutlinedTextField(
            value = startDurationTimeState,
            onValueChange = {},
            readOnly = true,
            placeholder = { Text(stringResource(R.string.start_duration)) },
            label = { Text(stringResource(R.string.start_duration)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "Start Time"
                )
            },
            interactionSource = startDurationSource,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // End Duration
        Text(text = stringResource(R.string.end_duration), style = MaterialTheme.typography.bodyMedium)
        OutlinedTextField(
            value = endDurationTimeState,
            onValueChange = { },
            placeholder = { Text(stringResource(R.string.end_duration)) },
            label = { Text(stringResource(R.string.end_duration)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Timer,
                    contentDescription = "End Time"
                )
            },
            readOnly = true,
            interactionSource = endDurationSource,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (errorState.isNotEmpty()){
            Text(text = errorState,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Red
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Radio Buttons
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = stringResource(R.string.notify_me_by),
                style = MaterialTheme.typography.bodyLarge
            )

            Row {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { selectedOption = Alert.ALARM.getLocalizedValue() }
                ) {
                    RadioButton(
                        selected = selectedOption == Alert.ALARM.getLocalizedValue(),
                        onClick = { selectedOption = Alert.ALARM.getLocalizedValue() }
                    )
                    Text(text = Alert.ALARM.getLocalizedValue(), modifier = Modifier.padding(start = 4.dp))
                }


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { selectedOption = Alert.NOTIFICATION.getLocalizedValue() }
                ) {
                    RadioButton(
                        selected = selectedOption == Alert.NOTIFICATION.getLocalizedValue(),
                        onClick = { selectedOption = Alert.NOTIFICATION.getLocalizedValue() }
                    )
                    Text(text = Alert.NOTIFICATION.getLocalizedValue(), modifier = Modifier.padding(start = 4.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.weight(0.5f))

            Button(
                onClick = {
                    val result = validation(startDurationTimeState, endDurationTimeState)

                    when (result) {
                        AlertErrors.START_DURATION_EMPTY -> {
                            errorState = context.getString(R.string.start_duration_is_empty)
                        }
                        AlertErrors.END_DURATION_EMPTY -> {
                            errorState = context.getString(R.string.end_duration_is_empty)
                        }
                        AlertErrors.DURATION_IN_PAST -> {
                            errorState = context.getString(R.string.duration_must_be_in_the_future)
                        }
                        AlertErrors.END_DURATION_BEFORE_START -> {
                            errorState = context.getString(R.string.end_time_is_before_start_time)
                        }
                        AlertErrors.NO_ERROR -> {
                                onSaved(
                                    startDurationTimeState,
                                    endDurationTimeState,
                                    Alert.getAbsoluteValue(selectedOption)
                                )
                        }
                    }
                    
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(greenColor),
                modifier = Modifier.weight(3.2f)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Save"
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(stringResource(R.string.save))
            }

            Spacer(modifier = Modifier.weight(0.5f))

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
                Text(stringResource(R.string.cancel))
            }

            Spacer(modifier = Modifier.weight(0.5f))
        }
    }
}

fun validation(startDuration: String, endDuration: String): AlertErrors {
    val calendar = Calendar.getInstance()
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
    val currentMinute = calendar.get(Calendar.MINUTE)

    return when {
        startDuration.isEmpty() -> AlertErrors.START_DURATION_EMPTY
        endDuration.isEmpty() -> AlertErrors.END_DURATION_EMPTY

        else -> {
            val (startHour, startMinute) = startDuration.split(":").map { it.toInt() }
            val (endHour, endMinute) = endDuration.split(":").map { it.toInt() }

            when {
                startHour < currentHour || (startHour == currentHour && startMinute < currentMinute) -> AlertErrors.DURATION_IN_PAST
                endHour < currentHour || (endHour == currentHour && endMinute < currentMinute) -> AlertErrors.DURATION_IN_PAST
                endHour < startHour || (endHour == startHour && endMinute < startMinute) -> AlertErrors.END_DURATION_BEFORE_START
                else -> AlertErrors.NO_ERROR
            }
        }
    }
}

fun showTimePicker(
    context: Context,
    initialHour: Int,
    initialMinute: Int,
    onTimeSelected: (String) -> Unit
) {
    val lightThemeContext =
        android.view.ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog)

    TimePickerDialog(
        lightThemeContext,
        { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
            onTimeSelected(String.format("%02d:%02d", selectedHour, selectedMinute))
        },
        initialHour,
        initialMinute,
        true
    ).show()
}


@Preview(locale = "es" , showBackground = true , showSystemUi = true)
@Composable
fun PreviewAlarmBottomSheet() {
    AlarmBottomSheet({} , {s,e,se->})
}

