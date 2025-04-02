package com.abdok.atmosphere.screens.settings

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.abdok.atmosphere.R
import com.abdok.atmosphere.enums.Languages
import com.abdok.atmosphere.enums.Locations
import com.abdok.atmosphere.enums.Speeds
import com.abdok.atmosphere.enums.Units
import com.abdok.atmosphere.screens.settings.componnts.SegmentedControlSection
import com.abdok.atmosphere.screens.settings.componnts.SwitchSection
import com.abdok.atmosphere.ui.components.CurvedNavBar
import com.abdok.atmosphere.utils.localization.LanguageManager
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onMapSelected: () -> Unit
) {
    val context = LocalContext.current

    val lifecycleOwner = LocalLifecycleOwner.current

    val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    val locationServicesState = remember { mutableStateOf(isLocationServicesEnabled(context)) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val isGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
                    || permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            if (isGranted) {
                if (!locationServicesState.value) {
                    openLocationSettings(context)
                }
                else{
                    viewModel.updateLocation(Locations.getValue(Locations.Gps.value))
                }
            } else {
                Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    )

    val isPermissionGranted = locationPermissions.any {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED){
            locationServicesState.value = isLocationServicesEnabled(context)
        }
    }



    LaunchedEffect(Unit) {
        CurvedNavBar.mutableNavBarState.emit(true)
    }

    LaunchedEffect(Unit) {
        viewModel.refreshValues()
    }

    val selectedTemperature by viewModel.temperature.collectAsStateWithLifecycle()
    val selectedWindSpeed by viewModel.windSpeed.collectAsStateWithLifecycle()
    val selectedLanguage by viewModel.language.collectAsStateWithLifecycle()
    val selectedLocation by viewModel.location.collectAsStateWithLifecycle()
    val isAnimation by viewModel.isAnimation.collectAsStateWithLifecycle()

    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = true
        )
    }

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
                    if (!isPermissionGranted) {
                        permissionLauncher.launch(locationPermissions)
                    } else {
                        if (!locationServicesState.value) {
                            openLocationSettings(context)
                        }
                        else{
                            viewModel.updateLocation(it)
                        }
                    }

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

        Spacer(modifier = Modifier.height(32.dp))

        SwitchSection(switchIcon =Icons.Default.Animation
            , switchText = stringResource(R.string.show_animated_background),
            isSwitchChecked =isAnimation
            , onSwitchToggled = viewModel::updateAnimation
        )


    }
}

private fun isLocationServicesEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}

private fun openLocationSettings(context: Context) {
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    context.startActivity(intent)
}

