package com.abdok.atmosphere.View.Screens.Settings

import android.telephony.CarrierConfigManager.Gps
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abdok.atmosphere.Enums.Languages
import com.abdok.atmosphere.Enums.Locations
import com.abdok.atmosphere.Enums.Speeds
import com.abdok.atmosphere.Enums.Units
import com.abdok.atmosphere.R
import com.abdok.atmosphere.Utils.LanguageManager


@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel
) {
    val context = LocalContext.current

    val selectedTemperature by viewModel.temperature.collectAsStateWithLifecycle()
    val selectedWindSpeed by viewModel.windSpeed.collectAsStateWithLifecycle()
    val selectedLanguage by viewModel.language.collectAsStateWithLifecycle()
    val selectedLocation by viewModel.location.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 48.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        SegmentedControlSection(
            title = stringResource(R.string.language),
            options = listOf(
                Languages.ENGLISH.value,
                Languages.ARABIC.value,
                Languages.SPANISH.value
            ),
            selectedOption = selectedLanguage,
            onOptionSelected = {
                viewModel.updateLanguage(it)
                LanguageManager.restartActivity(context)
            }
        )
        Spacer(modifier = Modifier.height(32.dp))
        SegmentedControlSection(
            title = stringResource(R.string.temperature_unit),
            options = listOf(Units.getDegreeByValue(Units.METRIC.value),
                Units.getDegreeByValue(Units.STANDARD.value),
                Units.getDegreeByValue(Units.IMPERIAL.value)),
            selectedOption = selectedTemperature,
            onOptionSelected = viewModel::updateTemperature
        )
        Spacer(modifier = Modifier.height(32.dp))

        SegmentedControlSection(
            title = stringResource(R.string.location),
            options = listOf(Locations.getValue(Locations.Gps.value), Locations.getValue(Locations.Map.value)),
            selectedOption = selectedLocation,
            onOptionSelected = viewModel::updateLocation
        )
        Spacer(modifier = Modifier.height(32.dp))

        SegmentedControlSection(
            title = stringResource(R.string.wind_speed_unit),
            options = listOf(Speeds.getDegree(Speeds.METERS_PER_SECOND.degree), Speeds.getDegree(Speeds.MILES_PER_HOUR.degree)),
            selectedOption = selectedWindSpeed,
            onOptionSelected = viewModel::updateWindSpeed
        )


    }
}

@Composable
fun SegmentedControlSection(
    title: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(24.dp))
                //.background(Color(0xFFF0F0F0))
            ,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            options.forEach { option ->
                val isSelected = option == selectedOption
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(if (isSelected) Color.Black else Color.Transparent)
                        .clickable {
                            onOptionSelected(option)
                        }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = option,
                        color = if (isSelected) Color.White else Color.Gray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
