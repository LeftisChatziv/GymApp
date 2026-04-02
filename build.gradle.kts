// Root build.gradle.kts

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // Firebase plugin
    id("com.google.gms.google-services") version "4.4.0" apply false
}

// Δεν χρειάζεται να βάζουμε repositories εδώ αν έχουμε ορίσει στο settings.gradle.kts
// Μπορείς να τα αφήσεις για παλαιότερα plugins αν χρειαστεί
allprojects {
    // ΑΠΟΦΥΓΕΤΕ ΑΥΤΟ αν έχετε repositories στο settings.gradle.kts
    // repositories {
    //     google()
    //     mavenCentral()
    // }
}

// buildscript block για παλιά plugins που χρειάζονται classpath (σπάνια πλέον)
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.0") // Google Services plugin
    }
}