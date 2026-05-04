package com.example.myapplication.navigation

import androidx.compose.material3.* import androidx.compose.material3.DrawerValue import androidx.compose.material3.ModalNavigationDrawer import androidx.compose.material3.ModalDrawerSheet import androidx.compose.material3.NavigationDrawerItem import androidx.compose.material3.Divider import androidx.compose.material3.Switch import androidx.compose.material3.Scaffold import androidx.compose.material3.TopAppBar import androidx.compose.material3.IconButton import androidx.compose.material3.Text import androidx.compose.material3.ExperimentalMaterial3Api import androidx.compose.runtime.* import androidx.compose.ui.Modifier import androidx.compose.ui.unit.dp import androidx.compose.ui.Alignment import androidx.navigation.compose.* import kotlinx.coroutines.launch import com.example.myapplication.viewmodel.ProgramViewModel import androidx.lifecycle.viewmodel.compose.viewModel import com.example.myapplication.viewmodel.ExerciseViewModel import androidx.compose.foundation.layout.height import androidx.compose.foundation.layout.Spacer import androidx.compose.foundation.layout.Arrangement import androidx.compose.foundation.layout.Row import androidx.compose.foundation.layout.fillMaxWidth import androidx.compose.foundation.layout.padding import com.example.myapplication.Screens.HomeScreen import com.example.myapplication.Screens.NavBar import com.example.myapplication.Screens.LoginScreen import com.example.myapplication.Screens.RegisterScreen import com.example.myapplication.Screens.ExercisesScreen import com.example.myapplication.Screens.ProgramScreen import androidx.compose.foundation.layout.Box import androidx.compose.foundation.layout.fillMaxSize import com.example.myapplication.Screens.MusicScreen import com.example.myapplication.Screens.ProfileScreen import com.example.myapplication.Screens.ProgramDetailsScreen import com.example.myapplication.Screens.ProgressScreen import com.example.myapplication.Screens.WorkoutScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(
    isDarkTheme: Boolean,
    onToggleTheme: (Boolean) -> Unit
) {

    val navController = rememberNavController()

    var notificationsEnabled by remember { mutableStateOf(true) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val programViewModel: ProgramViewModel = viewModel()
    val exerciseViewModel: ExerciseViewModel = viewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarRoutes = listOf(
        "home", "exercises", "program", "progress", "profile"
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {

            ModalDrawerSheet {

                Spacer(Modifier.height(16.dp))
                Text("⚙ Settings", style = MaterialTheme.typography.titleLarge)
                Divider()

                NavigationDrawerItem(
                    label = { Text("🎧 Music") },
                    selected = currentRoute == "music",
                    onClick = {
                        navController.navigate("music") {
                            launchSingleTop = true
                        }
                        scope.launch { drawerState.close() }
                    }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("🌗 Dark Mode")
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = onToggleTheme
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("🔔 Notifications")
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it }
                    )
                }
            }
        }
    ) {

        Scaffold(

            topBar = {
                TopAppBar(
                    title = { Text("My Gym App") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Text("☰")
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
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No workout selected")
                        }
                        return@composable
                    }

                    WorkoutScreen(
                        program = program,
                        onGoToProgress = { navController.navigate("progress") }
                    )
                }

                // 🔥 FIXED MUSIC SCREEN
                composable("music") {
                    MusicScreen(
                        onBackClick = { navController.popBackStack() }
                    )
                }

                composable("progress") { ProgressScreen() }

                composable("profile") { ProfileScreen() }

                composable("exercises") { ExercisesScreen() }
            }
        }
    }
}