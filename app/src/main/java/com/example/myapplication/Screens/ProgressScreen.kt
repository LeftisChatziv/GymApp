package com.example.myapplication.Screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.viewmodel.*

@Composable
fun ProgressScreen() {

    val progressViewModel: ProgressViewModel = viewModel()
    val programViewModel: ProgramViewModel = viewModel()
    val exerciseViewModel: ExerciseViewModel = viewModel()

    val userViewModel: UserViewModel = viewModel()

    // ================= FIREBASE INIT =================
    LaunchedEffect(Unit) {
        userViewModel.loadUser()
    }

    val exercises by exerciseViewModel.exercises.collectAsState()
    val programs by programViewModel.programs.collectAsState()
    val history by progressViewModel.history.collectAsState()

    val userWeightViewModel: UserPrefsViewModel =
        viewModel(
            factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(
                LocalContext.current.applicationContext as android.app.Application
            )
        )

    val userWeight by userWeightViewModel.userWeight.collectAsState()

    // ================= FIREBASE STATE =================
    val score by userViewModel.score.collectAsState()
    val streak by userViewModel.streak.collectAsState()
    val totalWorkouts by userViewModel.totalWorkouts.collectAsState()

    // ================= DATA =================
    val allProgramExercises = remember(programs) {
        programs.flatMap { it.exercises }
    }

    val muscleLoads = remember(allProgramExercises, exercises, userWeight) {
        if (allProgramExercises.isNotEmpty() && exercises.isNotEmpty()) {
            progressViewModel.calculateMuscleLoad(
                allProgramExercises,
                exercises,
                userWeight
            )
        } else emptyMap()
    }

    val weeklyVolume = remember(history) {
        progressViewModel.calculateWeeklyVolume(history)
    }

    val recovery = remember(history, score) {
        progressViewModel.calculateRecoveryScore(history, score)
    }

    val scrollState = rememberScrollState()

    val isLandscape =
        LocalConfiguration.current.orientation ==
                Configuration.ORIENTATION_LANDSCAPE

    Scaffold { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("Progress", style = MaterialTheme.typography.headlineMedium)

            Spacer(Modifier.height(16.dp))

            // ================= DEBUG =================
            Text("Programs: ${programs.size}")
            Text("Exercises: ${exercises.size}")
            Text("User Weight: $userWeight")
            Text("Score: $score")
            Text("Streak: $streak")
            Text("Total Workouts: $totalWorkouts")

            Spacer(Modifier.height(16.dp))

            // ================= ⭐ RANK PROGRESS BAR =================
            RankProgressBar(score = score)

            Spacer(Modifier.height(20.dp))

            // ================= HEATMAP =================
            if (isLandscape) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Card(modifier = Modifier.weight(1f)) {
                        Box(
                            Modifier.fillMaxSize().padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            BodyHeatmap(muscleLoads)
                        }
                    }

                    Card(modifier = Modifier.weight(1f)) {
                        Box(
                            Modifier.fillMaxSize().padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            WeeklyVolumeChart(data = weeklyVolume)
                        }
                    }
                }

            } else {

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                    Card(
                        Modifier
                            .fillMaxWidth()
                            .heightIn(350.dp, 520.dp)
                    ) {
                        Box(
                            Modifier.fillMaxSize().padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            BodyHeatmap(muscleLoads)
                        }
                    }

                    Card(
                        Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                    ) {
                        Box(
                            Modifier.fillMaxSize().padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            WeeklyVolumeChart(data = weeklyVolume)
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // ================= RECOVERY =================
            Card(Modifier.fillMaxWidth()) {

                Column(Modifier.padding(16.dp)) {

                    Text("Recovery Score")

                    Text(
                        "${recovery.toInt()}%",
                        style = MaterialTheme.typography.headlineMedium,
                        color = when {
                            recovery > 70 -> Color(0xFF2E7D32)
                            recovery > 40 -> Color(0xFFF9A825)
                            else -> Color(0xFFC62828)
                        }
                    )
                }
            }

            Spacer(Modifier.height(30.dp))
        }
    }
}