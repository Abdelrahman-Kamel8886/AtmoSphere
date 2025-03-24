package com.abdok.atmosphere.View.Screens.Settings

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abdok.atmosphere.Enums.Languages
import com.abdok.atmosphere.Enums.Locations
import com.abdok.atmosphere.Enums.Speeds
import com.abdok.atmosphere.Enums.Units
import com.abdok.atmosphere.Utils.LanguageManager

@Composable
fun SettingsScreen() {
    val context = LocalContext.current

    var selectedTemperature by remember { mutableStateOf(Units.METRIC.degree) }
    var selectedWindSpeed by remember { mutableStateOf(Speeds.METERS_PER_SECOND.degree) }
    var selectedLanguage by remember { mutableStateOf(Languages.ENGLISH.value) }
    var selectedLocation by remember { mutableStateOf(Locations.Gps.value) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp)
            .padding(top = 48.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        SegmentedControlSection(
            title = "Language",
            options = listOf(
                Languages.ENGLISH.value,
                Languages.ARABIC.value,
                Languages.SPANISH.value
            ),
            selectedOption = selectedLanguage,
            onOptionSelected = { selectedLanguage = it }
        )
        Spacer(modifier = Modifier.height(32.dp))
        SegmentedControlSection(
            title = "Temperature",
            options = listOf(Units.METRIC.degree, Units.IMPERIAL.degree, Units.STANDARD.degree),
            selectedOption = selectedTemperature,
            onOptionSelected = {
                Log.i("languageCode", "SettingsScreen: $it")
                selectedTemperature = it
                when (it) {
                    Languages.ENGLISH.value -> LanguageManager.setLocale(context, Languages.ENGLISH.code)
                    Languages.ARABIC.value -> LanguageManager.setLocale(context, Languages.ARABIC.code)
                    Languages.SPANISH.value -> LanguageManager.setLocale(context, Languages.SPANISH.code)
                }
                Handler(Looper.getMainLooper()).post {
                    (context as? Activity)?.recreate()
                }
            }
        )
        Spacer(modifier = Modifier.height(32.dp))

        SegmentedControlSection(
            title = "Location",
            options = listOf(Locations.Gps.value, Locations.Map.value),
            selectedOption = selectedLocation,
            onOptionSelected = { selectedLocation = it }
        )
        Spacer(modifier = Modifier.height(32.dp))

        SegmentedControlSection(
            title = "Wind speed",
            options = listOf(Speeds.METERS_PER_SECOND.degree, Speeds.MILES_PER_HOUR.degree),
            selectedOption = selectedWindSpeed,
            onOptionSelected = { selectedWindSpeed = it }
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
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFF0F0F0)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            options.forEach { option ->
                val isSelected = option == selectedOption
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(24.dp))
                        .background(if (isSelected) Color.Black else Color.Transparent)
                        .clickable {
                            onOptionSelected(option)
                        }
                        .padding(vertical = 12.dp),
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

@Preview(showBackground = true)
@Composable
fun PreviewSettingsScreen() {
    SettingsScreen()
}
