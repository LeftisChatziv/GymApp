package com.example.myapplication.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.myapplication.Screens.*
import com.example.myapplication.viewmodel.ProgramViewModel
import com.example.myapplication.viewmodel.ExerciseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(
    isDarkTheme: Boolean,
    onToggleTheme: (Boolean) -> Unit
) {

    val navController = rememberNavController()

    val programViewModel: ProgramViewModel = viewModel()
    val exerciseViewModel: ExerciseViewModel = viewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarRoutes = listOf(
        "home",
        "exercises",
        "program",
        "progress",
        "profile"
    )

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
            if (currentRoute in bottomBarRoutes) {
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

            composable("exercises") {
                ExercisesScreen()
            }

            composable("program") {
                ProgramScreen(
                    onOpenProgram = { program ->
                        programViewModel.selectProgram(program.program.id)
                        navController.navigate("program_details")
                    }
                )
            }

            composable("program_details") {

                val programs by programViewModel.programs.collectAsState()
                val selectedId by programViewModel.selectedProgramId.collectAsState()
                val exercisesPool by exerciseViewModel.exercises.collectAsState(initial = emptyList())

                val program = programs.firstOrNull { it.program.id == selectedId }

                if (program == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No program selected")
                    }
                    return@composable
                }

                ProgramDetailsScreen(
                    program = program,
                    exercisesPool = exercisesPool,
                    programViewModel = programViewModel,
                    onStart = { navController.navigate("workout") },
                    onBack = { navController.popBackStack() }
                )
            }

            composable("workout") {

                val programs by programViewModel.programs.collectAsState()
                val selectedId by programViewModel.selectedProgramId.collectAsState()

                val program = programs.firstOrNull { it.program.id == selectedId }

                if (program == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No workout selected")
                    }
                    return@composable
                }

                WorkoutScreen(program)
            }

            composable("progress") {
                ProgressScreen()
            }

            composable("profile") {
                ProfileScreen()
            }
        }
    }
}