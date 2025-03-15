package com.abdok.atmosphere

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.abdok.atmosphere.Data.DataSources.RemoteDataSource
import com.abdok.atmosphere.Data.Remote.RetroConnection
import com.abdok.atmosphere.Ui.CurvedNavBar
import com.abdok.atmosphere.Ui.Home.HomeViewModel
import com.abdok.atmosphere.Ui.Home.HomeViewModelFactory


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
           MainScreen()

        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            CurvedNavBar(navController)
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            setupNavHost(navController)
        }
    }
}

@Preview
@Composable
fun hamada(){
    ConstraintLayout(Modifier.fillMaxSize()){

        val (text1, text2 , text3) = createRefs()
        
        Text(text = "text1", Modifier.constrainAs(text1) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })

        Text(text = "text2", Modifier.constrainAs(text2) {
            top.linkTo(text1.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })

        Text(text = "text3", Modifier.constrainAs(text3) {
            top.linkTo(text2.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })
    }
}




