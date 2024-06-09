plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.devtools.ksp")
    id ("kotlin-parcelize")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.dicoding.nutriseeapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dicoding.nutriseeapp"
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
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.room.common)
    testImplementation(libs.junit)
    implementation(libs.de.hdodenhof.circleimageview3)
    implementation (libs.androidx.navigation.fragment.ktx)
    implementation (libs.androidx.navigation.ui.ktx)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //api
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.logging.interceptor)
    implementation (libs.glide)
    //data
    implementation (libs.androidx.datastore.preferences)
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.extensions)
    //lottie
    implementation (libs.lottie)
    //save information
    implementation (libs.androidx.datastore.preferences)
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.androidx.lifecycle.livedata.ktx)
    //activity
    implementation (libs.androidx.activity.ktx)
    //bottom nav
    implementation (libs.androidx.navigation.fragment.ktx)
    implementation (libs.androidx.navigation.ui.ktx)
    //camera
    implementation (libs.androidx.camera.camera2)
    implementation (libs.androidx.camera.lifecycle)
    implementation (libs.androidx.camera.view)
    //image
    implementation (libs.glide.v4150)
    annotationProcessor (libs.compiler)
    implementation (libs.de.hdodenhof.circleimageview3)
    //gps
    implementation (libs.play.services.maps.v1820)
    implementation (libs.play.services.location.v1800)
    //paging
    implementation (libs.androidx.paging.runtime.ktx)
    //room
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    //firebase
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation("com.google.firebase:firebase-auth")
    //ucrop
    implementation ("com.github.yalantis:ucrop:2.2.8")
}