package com.abdok.atmosphere.ui.screens.settings

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abdok.atmosphere.enums.Languages
import com.abdok.atmosphere.enums.Locations
import com.abdok.atmosphere.enums.Speeds
import com.abdok.atmosphere.enums.Units
import com.abdok.atmosphere.R
import com.abdok.atmosphere.utils.localization.LanguageManager
import com.abdok.atmosphere.ui.CurvedNavBar


@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onMapSelected: () -> Unit
) {
    val context = LocalContext.current

    CurvedNavBar.mutableNavBarState.value = true

    LaunchedEffect(Unit) {
        viewModel.refreshValues()
    }

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
            icon = Icons.Default.Language,
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
            icon = Icons.Default.Thermostat,
            onOptionSelected = viewModel::updateTemperature
        )
        Spacer(modifier = Modifier.height(32.dp))

        SegmentedControlSection(
            title = stringResource(R.string.location),
            options = listOf(Locations.getValue(Locations.Gps.value), Locations.getValue(Locations.Map.value)),
            selectedOption = selectedLocation,
            icon = Icons.Default.LocationOn,
            onOptionSelected = {

                if (it == Locations.getValue(Locations.Map.value)) {
                    onMapSelected()
                }else{
                    viewModel.updateLocation(it)
                }
            }
        )
        Spacer(modifier = Modifier.height(32.dp))

        SegmentedControlSection(
            title = stringResource(R.string.wind_speed_unit),
            options = listOf(Speeds.getDegree(Speeds.METERS_PER_SECOND.degree), Speeds.getDegree(Speeds.MILES_PER_HOUR.degree)),
            selectedOption = selectedWindSpeed,
            icon = Icons.Default.Air,
            onOptionSelected = viewModel::updateWindSpeed
        )


    }
}
@Composable
fun SegmentedControlSection(
    title: String ,
    options: List<String> ,
    selectedOption: String ,
    icon : ImageVector,
    onOptionSelected: (String) -> Unit
) {
    Column {
        Row {
            Icon(imageVector = icon,
                tint = Color.Gray
                , contentDescription = null)

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
                , modifier = Modifier.padding(start = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(24.dp))
                .background(Color(0xFFF0F0F0))
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
