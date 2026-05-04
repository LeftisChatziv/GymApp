package com.example.myapplication.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.navigation.compose.*
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.compose.foundation.layout.*
import androidx.compose.ui.platform.LocalContext

import com.example.myapplication.viewmodel.*
import com.example.myapplication.Screens.*

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

    val context = LocalContext.current

    val programViewModel: ProgramViewModel = viewModel()
    val exerciseViewModel: ExerciseViewModel = viewModel()

    // ✅ GLOBAL MUSIC VIEWMODEL (NO CRASH)
    val musicViewModel: MusicViewModel = viewModel()

    val currentPosition by musicViewModel.currentPosition.collectAsState()
    val duration by musicViewModel.duration.collectAsState()

    var isPlaying by remember { mutableStateOf(false) }

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
                HorizontalDivider()

                // 🎧 GO TO MUSIC SCREEN
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

                // ================= 🎵 MINI PLAYER =================

                HorizontalDivider()
                Spacer(Modifier.height(10.dp))

                Text(
                    "🎵 Now Playing",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )

                if (duration > 0) {

                    val progress =
                        (currentPosition / duration.toFloat()).coerceIn(0f, 1f)

                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                        Slider(
                            value = progress,
                            onValueChange = {
                                musicViewModel.seekTo((it * duration).toInt())
                            }
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(formatMiniTime(currentPosition))
                            Text(formatMiniTime(duration))
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    Button(onClick = {
                        musicViewModel.previous(context)
                        isPlaying = true
                    }) {
                        Text("⏮")
                    }

                    Button(onClick = {
                        if (isPlaying) {
                            musicViewModel.pause()
                        } else {
                            musicViewModel.resume()
                        }
                        isPlaying = !isPlaying
                    }) {
                        Text(if (isPlaying) "Pause" else "Play")
                    }

                    Button(onClick = {
                        musicViewModel.next(context)
                        isPlaying = true
                    }) {
                        Text("⏭")
                    }
                }

                // ================= SETTINGS =================

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

                composable("music") {
                    MusicScreen(
                        viewModel = musicViewModel,
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

// ⏱ TIME FORMAT
fun formatMiniTime(ms: Int): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}