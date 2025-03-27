buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.22")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    kotlin("plugin.serialization") version "2.1.10"
    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false
}