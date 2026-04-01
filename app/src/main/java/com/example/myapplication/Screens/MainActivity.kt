package com.example.myapplication.Screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                GymApp()
            }
        }
    }
}

@Composable
fun GymApp() {
    var currentScreen by remember { mutableStateOf("loading") }

    // Loading screen για 2 δευτερόλεπτα
    LaunchedEffect(Unit) {
        delay(2000)
        currentScreen = "login"
    }

    when (currentScreen) {
        "loading" -> LoadingScreen()
        "login" -> LoginScreen(
            onGoToRegister = { currentScreen = "register" },
            onLoginSuccess = { currentScreen = "home" }
        )
        "register" -> RegisterScreen(
            onGoToLogin = { currentScreen = "login" }
        )
        "home" -> HomeScreen(
            onLogout = { currentScreen = "login" }
        )
    }
}