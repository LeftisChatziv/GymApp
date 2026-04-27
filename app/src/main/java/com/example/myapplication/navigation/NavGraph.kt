package com.example.myapplication.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.example.myapplication.Screens.*
import com.example.myapplication.Screens.ProgramScreen
import com.example.myapplication.screens.ProgressScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(
    isDarkTheme: Boolean,
    onToggleTheme: (Boolean) -> Unit
) {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val noBottomBarScreens = listOf("login", "register")

    Scaffold(

        topBar = {
            TopAppBar(
                title = { Text("My Gym App") },
                actions = {
                    IconButton(onClick = { onToggleTheme(!isDarkTheme) }) {
                        Text(if (isDarkTheme) "☀️" else "🌙")
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
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
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
                            popUpTo(0)
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