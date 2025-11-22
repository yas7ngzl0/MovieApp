// IMPORTS ARE FIRST
import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

// read local properties file
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {

    FileInputStream(localPropertiesFile).use { fis ->
        localProperties.load(fis)
    }
}

// api key cannot be null
val tmdbApiKey: String = localProperties.getProperty("tmdb_api_key") ?: ""

android {
    namespace = "com.yasinguzel.movieapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.yasinguzel.movieapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // api key will be a regular string
        buildConfigField(
            "String",
            "TMDB_API_KEY",
            "\"$tmdbApiKey\""
        )
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
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

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("androidx.navigation:navigation-compose:2.7.5")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("com.google.dagger:hilt-android:2.48")
    //kapt("com.google.dagger:hilt-android-compiler:2.48")

    testImplementation(libs.junit)
    // Unit Testing Libraries
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.13.8") // Best library of kotlin for mock
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3") // To make corutines test
    testImplementation("androidx.arch.core:core-testing:2.2.0") // Live Data test

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
