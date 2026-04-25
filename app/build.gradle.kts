
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 35   // ✅ FIX

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
        targetSdk = 35   // ✅ FIX
        versionCode = 1
        versionName = "1.0"
    }


    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation("androidx.compose.material:material-icons-extended")
    // COMPOSE
    implementation(platform("androidx.compose:compose-bom:2024.09.00"))
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(libs.firebase.crashlytics.buildtools)
    implementation(libs.androidx.compose.runtime)

    debugImplementation("androidx.compose.ui:ui-tooling")

    // NAVIGATION
    implementation("androidx.navigation:navigation-compose:2.8.0")

    // ROOM
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // FIREBASE (FIXED)
    implementation(platform("com.google.firebase:firebase-bom:34.11.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")

    // TEST
    testImplementation("junit:junit:4.13.2")
}