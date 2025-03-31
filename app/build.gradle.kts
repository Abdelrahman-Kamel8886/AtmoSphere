import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin ("plugin.serialization") version "2.1.10"
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.10"
    id ("kotlin-kapt")
}


android {
    namespace = "com.abdok.atmosphere"
    compileSdk = 35

    val file = rootProject.file("local.properties")
    val properties = Properties()
    properties.load(FileInputStream(file))

    defaultConfig {
        applicationId = "com.abdok.atmosphere"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        buildConfigField("String", "API_KEY", properties.getProperty("API_KEY"))
        buildConfigField("String", "MAP_API_KEY", properties.getProperty("MAP_API_KEY"))
        resValue ("string", "maps_api_key", properties.getProperty("MAP_API_KEY"))
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.places)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //curved bottom navigation bar
    implementation (libs.curved.bottom.navigation)

    //compose navigation & serialization
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.1.0")
    implementation("androidx.navigation:navigation-compose:2.8.8")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")

    //To use constraintlayout in compose
    implementation ("androidx.constraintlayout:constraintlayout-compose:1.1.1")

    //gson
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    //okhttp3
    implementation ("com.squareup.okhttp3:okhttp:4.9.3")

    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    //Glide
    implementation ("com.github.bumptech.glide:compose:1.0.0-beta01")

    //WorkManager
    implementation ("androidx.work:work-runtime-ktx:2.7.1")


    //
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")


    //Room Database
    implementation ("androidx.room:room-runtime:2.6.1")
    kapt ("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")

    //LiveData & Compose
    val compose_version = "1.0.0"
    implementation ("androidx.compose.runtime:runtime-livedata:$compose_version")

    //compose viewmodel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose-android:2.8.7")

    //lottie
    implementation ("com.airbnb.android:lottie-compose:6.1.0")


    // locations & map
    implementation ("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.google.maps.android:maps-compose:6.4.1")

    //workManger
    implementation("androidx.work:work-runtime:2.9.0")

    //places
    implementation("com.google.maps.android:places-compose:0.1.3")


    implementation("androidx.compose.material:material-icons-extended:1.7.8")

    
    implementation ("androidx.compose.material:material:1.5.4")

    implementation ("androidx.lifecycle:lifecycle-service:2.8.1")

    implementation( "com.google.accompanist:accompanist-systemuicontroller:0.31.1-alpha")



}

kapt {
    correctErrorTypes = true
}