package com.example.myapplication.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.example.myapplication.Screens.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(
    isDarkTheme: Boolean,
    onToggleTheme: (Boolean) -> Unit
) {

    val navController = rememberNavController()

    val currentRoute = navController
        .currentBackStackEntryAsState()
        .value
        ?.destination
        ?.route

    val noBottomBarScreens = listOf("login", "register")

    Scaffold(

        topBar = {
            TopAppBar(
                title = { Text("My Gym App") },
                actions = {
                    IconButton(
                        onClick = {
                            onToggleTheme(!isDarkTheme)
                        }
                    ) {
                        Icon(
                            imageVector =
                                if (isDarkTheme) Icons.Default.LightMode
                                else Icons.Default.DarkMode,
                            contentDescription = "toggle theme"
                        )
                    }
                }
            )
        },

        bottomBar = {
            if (currentRoute !in noBottomBarScreens) {
                NavBar(
                    selectedItem = currentRoute ?: "home",
                    onItemSelected = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                )
            }
        }

    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(padding)
        ) {

            composable("login") {
                LoginScreen(
                    onGoToRegister = { navController.navigate("register") },
                    onLoginSuccess = {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }

            composable("register") {
                RegisterScreen(
                    onGoToLogin = { navController.popBackStack() }
                )
            }

            composable("home") {
                HomeScreen(
                    onLogout = {
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                )
            }

            composable("exercises") { ExercisesScreen() }
            composable("program") { ProgramScreen() }
            composable("progress") { ProgressScreen() }
            composable("profile") { ProfileScreen() }
        }
    }
}
@Composable fun ProfileScreen() { TODO() }
@Composable fun ProgressScreen() { TODO() }
class ProgramScreen