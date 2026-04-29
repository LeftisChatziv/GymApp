package com.example.myapplication.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.viewmodel.ProgressViewModel
import com.example.myapplication.viewmodel.ProgramViewModel
import com.example.myapplication.viewmodel.ExerciseViewModel

@Composable
fun ProgressScreen() {

    val progressViewModel: ProgressViewModel = viewModel()
    val programViewModel: ProgramViewModel = viewModel()
    val exerciseViewModel: ExerciseViewModel = viewModel()

    val exercises by exerciseViewModel.exercises.collectAsState(initial = emptyList())
    val programs by programViewModel.programs.collectAsState()
    val selectedProgramId by programViewModel.selectedProgramId.collectAsState()

    // ================= SAFE PROGRAM =================
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

    // ================= SCROLL STATE =================
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

            // ================= BODY HEATMAP =================
            BodyHeatmap(
                muscleLoads = muscleLoads,
                vm = progressViewModel
            )

            Spacer(Modifier.height(24.dp))

            // ================= WEEKLY SECTION =================
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Weekly Progress",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Program: ${
                            selectedProgram?.program?.name ?: "No program selected"
                        }"
                    )

                    Text(
                        text = "Exercises: ${programExercises.size}"
                    )
                }
            }

            Spacer(Modifier.height(30.dp))
        }
    }
}