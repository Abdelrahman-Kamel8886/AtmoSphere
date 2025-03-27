package com.abdok.atmosphere

import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.abdok.atmosphere.Data.Local.SharedPreference.SharedPreferencesImpl
import com.abdok.atmosphere.Data.Response
import com.abdok.atmosphere.Utils.Constants
import com.abdok.atmosphere.Utils.Network.NetworkStateObserver
import com.abdok.atmosphere.Utils.ViewHelpers.setAlarm
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

    override fun onStart() {
        super.onStart()
/*        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivity(intent)
        }

        createNotificationChannel()
        setAlarm(this@MainActivity, 10000)*/
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "alarm_channel",
                "Alarm Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for Alarm Notifications"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
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


    override fun onDestroy() {
        super.onDestroy()

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainScreen(location: Location) {

        val networkStateObserver = remember { NetworkStateObserver(this@MainActivity) }
        val isConnected by networkStateObserver.isConnected.observeAsState(true)

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
                if (!isConnected) {
                    Row (Modifier.fillMaxWidth().padding(top = 40.dp)){

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .background(Color.Red, shape = RoundedCornerShape(4.dp))
                                .padding(vertical = 4.dp)
                            ,
                            text = "Offline Mood",
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}









