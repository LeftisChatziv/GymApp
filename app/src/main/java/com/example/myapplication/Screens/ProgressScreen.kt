package com.example.myapplication.Screens

import WeeklyVolumeChart
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.viewmodel.*

@Composable
fun ProgressScreen() {

    val progressViewModel: ProgressViewModel = viewModel()
    val programViewModel: ProgramViewModel = viewModel()
    val exerciseViewModel: ExerciseViewModel = viewModel()
    val userStatsViewModel: UserStatsViewModel = viewModel()

    val exercises by exerciseViewModel.exercises.collectAsState()
    val programs by programViewModel.programs.collectAsState()
    val selectedProgramId by programViewModel.selectedProgramId.collectAsState()
    val history by progressViewModel.history.collectAsState()
    val userStats by userStatsViewModel.stats.collectAsState()

    // ================= AUTO SELECT PROGRAM =================
    LaunchedEffect(programs, selectedProgramId) {
        if (selectedProgramId == null && programs.isNotEmpty()) {
            programViewModel.selectProgram(programs.first().program.id)
        }
    }

    // ================= SELECTED PROGRAM =================
    val selectedProgram = remember(programs, selectedProgramId) {
        programs.firstOrNull { it.program.id == selectedProgramId }
    }

    val programExercises = selectedProgram?.exercises.orEmpty()

    // =========================================================
    // 🔥 FIX: stable muscle load (NO recompute on every recomposition)
    // =========================================================
    val muscleLoads = remember(programExercises, exercises) {
        if (programExercises.isNotEmpty() && exercises.isNotEmpty()) {
            progressViewModel.calculateMuscleLoad(
                programExercises,
                exercises
            )
        } else {
            emptyMap()
        }
    }

    val weeklyVolume = remember(history) {
        progressViewModel.calculateWeeklyVolume(history)
    }

    val recovery = remember(history) {
        progressViewModel.calculateRecoveryScore(history)
    }

    val totalXp = userStats?.score ?: 0.0
    val rankProgress = calculateRankProgress(totalXp)

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

            Text(
                "Progress",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(16.dp))

            // ================= DEBUG =================
            Text("Selected Program ID: $selectedProgramId")
            Text("Programs: ${programs.size}")
            Text("Program exercises: ${programExercises.size}")
            Text("Exercises DB: ${exercises.size}")
            Text("MuscleLoads size: ${muscleLoads.size}")

            Spacer(Modifier.height(16.dp))

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
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            BodyHeatmap(muscleLoads)
                        }
                    }

                    Card(modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            WeeklyVolumeChart(data = weeklyVolume)
                        }
                    }
                }

            } else {

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(350.dp, 520.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            BodyHeatmap(muscleLoads)
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            WeeklyVolumeChart(data = weeklyVolume)
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // ================= STATS =================
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Weekly Progress")
                    Text("Exercises: ${history.size}")
                    Text("Sessions: ${history.size}")
                }
            }

            Spacer(Modifier.height(20.dp))

            // ================= RANK =================
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {

                    Text("Rank Progress")
                    Spacer(Modifier.height(8.dp))

                    Text("Current: ${rankProgress.currentRank}")

                    Text(
                        if (rankProgress.nextRank != null)
                            "Next: ${rankProgress.nextRank}"
                        else "Max Rank Reached 🔥"
                    )

                    Spacer(Modifier.height(12.dp))

                    LinearProgressIndicator(
                        progress = { rankProgress.progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                    )

                    Spacer(Modifier.height(8.dp))

                    Text("${(rankProgress.progress * 100).toInt()}%")
                }
            }

            Spacer(Modifier.height(20.dp))

            // ================= RECOVERY =================
            Card(modifier = Modifier.fillMaxWidth()) {

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