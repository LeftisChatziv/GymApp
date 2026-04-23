package com.example.myapplication.Screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.myapplication.navigation.NavGraph
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.runtime.saveable.rememberSaveable
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            // ✅ σωστό state (δεν χάνεται σε recomposition)
            var isDarkTheme by rememberSaveable { mutableStateOf(false) }

            MyApplicationTheme(darkTheme = isDarkTheme) {

                NavGraph(
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = { isDarkTheme = it }
                )
            }
        }
    }
}