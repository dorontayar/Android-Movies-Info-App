plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "il.ac.hit.android_movies_info_app"
    compileSdk = 34

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "il.ac.hit.android_movies_info_app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {
    //gson implementation
    implementation(libs.gson)
    //for dependency injection using hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    //for view model property delegate
    implementation (libs.androidx.fragment.ktx)
    implementation (libs.androidx.activity.ktx)
    //for coroutines
    implementation (libs.kotlinx.coroutines.android)
    implementation (libs.kotlinx.coroutines.core)
    //for firebase coroutine support
    implementation (libs.kotlinx.coroutines.play.services)
    //for flow as live data
    implementation (libs.androidx.lifecycle.livedata.ktx.v281)
    //firebase version control
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    // Firebase Firestore Database
    implementation (libs.firebase.firestore.ktx)
    // Firebase Auth
    implementation (libs.firebase.auth.ktx)
    //navigation - added when adding the resource
    implementation (libs.androidx.navigation.fragment.ktx)
    implementation (libs.androidx.navigation.ui.ktx)
    //For live data ktx - flow as liveData
    implementation (libs.lifecycle.livedata.ktx)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}