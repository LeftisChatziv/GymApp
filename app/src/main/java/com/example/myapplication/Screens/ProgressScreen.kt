package com.example.myapplication.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.viewmodel.ProgressViewModel
import com.example.myapplication.viewmodel.ProgramViewModel
import com.example.myapplication.viewmodel.ExerciseViewModel
import WeeklyVolumeChart

@Composable
fun ProgressScreen() {

    val progressViewModel: ProgressViewModel = viewModel()
    val programViewModel: ProgramViewModel = viewModel()
    val exerciseViewModel: ExerciseViewModel = viewModel()

    val exercises by exerciseViewModel.exercises.collectAsState(initial = emptyList())
    val programs by programViewModel.programs.collectAsState()
    val selectedProgramId by programViewModel.selectedProgramId.collectAsState()

    // ================= 🔥 REAL HISTORY FROM DB =================
    val history by progressViewModel.history.collectAsState()

    // ================= PROGRAM =================
    val selectedProgram = programs
        .firstOrNull { it.program.id == selectedProgramId }

    val programExercises = selectedProgram?.exercises ?: emptyList()

    // ================= MUSCLE LOAD =================
    val muscleLoads by remember(programExercises, exercises) {
        derivedStateOf {
            progressViewModel.calculateMuscleLoad(
                programExercises,
                exercises
            )
        }
    }

    // ================= WEEKLY VOLUME =================
    val weeklyVolume by remember(history) {
        derivedStateOf {
            progressViewModel.calculateWeeklyVolume(history)
        }
    }

    // ================= RECOVERY =================
    val recovery by remember(history) {
        derivedStateOf {
            progressViewModel.calculateRecoveryScore(history)
        }
    }

    val scrollState = rememberScrollState()

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
                text = "Progress",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(16.dp))

            // ================= HEATMAP =================
            BodyHeatmap(
                muscleLoads = muscleLoads,
                vm = progressViewModel
            )

            Spacer(Modifier.height(24.dp))

            // ================= CHART =================
            WeeklyVolumeChart(data = weeklyVolume)

            Spacer(Modifier.height(24.dp))

            // ================= INFO =================
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(Modifier.padding(16.dp)) {

                    Text("Weekly Progress")

                    Text("Program: ${selectedProgram?.program?.name ?: "No program selected"}")

                    Text("Exercises: ${programExercises.size}")

                    // 🔥 ΤΩΡΑ ΘΑ ΔΟΥΛΕΥΕΙ
                    Text("Sessions: ${history.size}")
                }
            }

            Spacer(Modifier.height(20.dp))

            // ================= RECOVERY =================
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(Modifier.padding(16.dp)) {

                    Text("Recovery Score")

                    Text(
                        text = "${recovery.toInt()}%",
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