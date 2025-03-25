package com.abdok.atmosphere

import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.abdok.atmosphere.View.Screens.CurvedNavBar
import com.abdok.atmosphere.WorkManger.LocationWorker
import com.google.android.gms.location.FusedLocationProviderClient
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.core.app.ActivityCompat
import com.abdok.atmosphere.Data.Local.SharedPreference.SharedPreferencesImpl
import com.abdok.atmosphere.Data.Response
import com.abdok.atmosphere.Utils.Constants
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import java.util.Locale


class MainActivity : ComponentActivity() {

    private final val REQUEST_LOCATION_CODE = 8886


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        applyLanguage()

        enableEdgeToEdge()
        if(checkLocationPermission()){
            if(isLocationEnabled()){
                scheduleLocationWorker()
            }
            else {
                enableLocation()
            }
        }
        else{
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION)
                ,REQUEST_LOCATION_CODE
            )
        }

        setContent{
            var locationState = LocationWorker.mutableLiveLocation.observeAsState()
            LaunchedEffect(Unit) {
            }

            when(locationState.value){
                is Response.Error -> {
                    val message = (locationState.value as Response.Error).exception
                    Box (Modifier.fillMaxSize() , contentAlignment = Alignment.Center){
                        Text(text = message)
                    }
                }
                Response.Loading -> {
                    Box (Modifier.fillMaxSize() , contentAlignment = Alignment.Center){
                        CircularProgressIndicator()
                    }
                }
                is Response.Success -> {
                    val location = (locationState.value as Response.Success).data
                    MainScreen(location)
                }
                null -> {
                    Box (Modifier.fillMaxSize() , contentAlignment = Alignment.Center){
                        Text(text = "Null")
                    }

                }
            }

           
        }
    }

    private fun applyLanguage(){
        val sharedPreferences = SharedPreferencesImpl.getInstance()
        val language = sharedPreferences.fetchData(Constants.LANGUAGE_CODE , Constants.DEFAULT_LANG)

        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }


    private fun checkLocationPermission() : Boolean  {
        return checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun enableLocation() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener { response ->
            scheduleLocationWorker()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    // Show the dialog to enable location
                    exception.startResolutionForResult(this, 123)
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123) {
            if (resultCode == Activity.RESULT_OK) {
                scheduleLocationWorker()
            } else {
                // User denied enabling location
            }
        }
    }

    private fun isLocationEnabled() : Boolean {
        val locationManager : LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }



    fun scheduleLocationWorker() {
        val workRequest = PeriodicWorkRequestBuilder<LocationWorker>(15, java.util.concurrent.TimeUnit.MINUTES)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED) // Only fetch when the network is available
                    .build()
            )
            .build()

        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                "LocationWorker",
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest
            )
    }



}

@Composable
fun MainScreen(location: Location) {
    val defaultBackground = Brush.linearGradient(
        listOf(Color(0xFFF5F5F5), Color(0xFFFFFFFF))
    )
    val navController = rememberNavController()
    val isNavBarVisible = CurvedNavBar.mutableNavBarState.observeAsState()
    //var background = SharedModel.screenBackground.observeAsState()
    Scaffold(
        bottomBar = {
            when(isNavBarVisible.value){
                true -> CurvedNavBar.ShowCurvedNavBar(navController)
                false -> {}
                null -> {}
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .background(defaultBackground)
        ) {
            setupNavHost(navController , location)
        }
    }
}









