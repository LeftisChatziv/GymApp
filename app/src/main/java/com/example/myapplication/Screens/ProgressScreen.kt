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

// ================= RANK PROGRESS =================


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

    val selectedProgram = remember(programs, selectedProgramId) {
        programs.firstOrNull { it.program.id == selectedProgramId }
    }

    val programExercises = remember(selectedProgram) {
        selectedProgram?.exercises ?: emptyList()
    }

    val muscleLoads = remember(programExercises, exercises) {
        progressViewModel.calculateMuscleLoad(programExercises, exercises)
    }

    val weeklyVolume = remember(history) {
        progressViewModel.calculateWeeklyVolume(history)
    }

    val recovery = remember(history) {
        progressViewModel.calculateRecoveryScore(history)
    }

    // ================= XP SOURCE FIX =================
    val totalXp = remember(userStats) {
        userStats?.score ?: 0.0
    }

    val rankProgress = remember(totalXp) {
        calculateRankProgress(totalXp)
    }

    val scrollState = rememberScrollState()
    val isLandscape =
        LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

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

            if (isLandscape) {

                Row(
                    Modifier.fillMaxWidth().height(260.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Card(Modifier.weight(1f)) {
                        Box(Modifier.fillMaxSize().padding(8.dp), Alignment.Center) {
                            BodyHeatmap(muscleLoads)
                        }
                    }

                    Card(Modifier.weight(1f)) {
                        Box(Modifier.fillMaxSize().padding(8.dp), Alignment.Center) {
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
                        Box(Modifier.fillMaxSize().padding(8.dp), Alignment.Center) {
                            BodyHeatmap(muscleLoads)
                        }
                    }

                    Card(Modifier.fillMaxWidth().height(220.dp)) {
                        Box(Modifier.fillMaxSize().padding(8.dp), Alignment.Center) {
                            WeeklyVolumeChart(data = weeklyVolume)
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // ================= WEEKLY =================
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Weekly Progress")
                    Text("Exercises: ${history.size}")
                    Text("Sessions: ${history.size}")
                }
            }

            Spacer(Modifier.height(20.dp))

            // ================= RANK PROGRESS =================
            Card(Modifier.fillMaxWidth()) {

                Column(Modifier.padding(16.dp)) {

                    Text("Rank Progress")

                    Spacer(Modifier.height(8.dp))

                    Text("Current: ${rankProgress.currentRank}")

                    if (rankProgress.nextRank != null) {
                        Text("Next: ${rankProgress.nextRank}")
                    } else {
                        Text("Max Rank Reached 🔥")
                    }

                    Spacer(Modifier.height(12.dp))

                    LinearProgressIndicator(
                        progress = rankProgress.progress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                    )

                    Spacer(Modifier.height(8.dp))

                    Text("${(rankProgress.progress * 100).toInt()}% to next rank")
                }
            }

            Spacer(Modifier.height(20.dp))

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