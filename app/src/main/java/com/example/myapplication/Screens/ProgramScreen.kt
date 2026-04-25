package com.example.myapplication.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.local.entity.Exercise
import com.example.myapplication.viewmodel.ExerciseViewModel
import com.example.myapplication.viewmodel.ProgramViewModel
import com.example.myapplication.viewmodel.ExercisePlan

// UI model
data class ExerciseConfig(
    val exercise: Exercise,
    var sets: Int = 3,
    var reps: Int = 10,
    var weight: Int = 0
)

@Composable
fun ProgramScreen(
    exerciseViewModel: ExerciseViewModel = viewModel(),
    programViewModel: ProgramViewModel = viewModel()
) {

    val exercises by exerciseViewModel.exercises.collectAsState(initial = emptyList())

    var showDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }

    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    val selectedDays = remember { mutableStateListOf<String>() }
    val selectedExercises = remember { mutableStateListOf<ExerciseConfig>() }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Text("+")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            Text(
                text = "Programs",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(16.dp))

            Text("Default Programs (demo)")
        }
    }

    // 🔥 DIALOG
    if (showDialog) {

        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {

                TextButton(onClick = {

                    val plan = selectedExercises.map {
                        ExercisePlan(
                            exerciseId = it.exercise.id,
                            sets = it.sets,
                            reps = it.reps,
                            weight = it.weight
                        )
                    }

                    programViewModel.createProgram(
                        title = title,
                        days = selectedDays.toList(),
                        exercises = plan
                    )

                    showDialog = false
                    title = ""
                    selectedDays.clear()
                    selectedExercises.clear()

                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Create Program") },
            text = {

                Column {

                    // 🔹 TITLE
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Program Title") }
                    )

                    Spacer(Modifier.height(12.dp))

                    // 🔹 DAYS GRID (FIXED)
                    Text("Select Days")

                    Spacer(Modifier.height(8.dp))

                    val rows = daysOfWeek.chunked(4)

                    Column {
                        rows.forEach { rowDays ->

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                rowDays.forEach { day ->

                                    val isSelected = selectedDays.contains(day)

                                    FilterChip(
                                        selected = isSelected,
                                        onClick = {
                                            if (isSelected)
                                                selectedDays.remove(day)
                                            else
                                                selectedDays.add(day)
                                        },
                                        label = { Text(day) },
                                        modifier = Modifier.padding(4.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    // 🔹 EXERCISES
                    Text("Select Exercises")

                    LazyColumn(
                        modifier = Modifier.height(220.dp)
                    ) {

                        items(exercises) { exercise ->

                            val isSelected =
                                selectedExercises.any { it.exercise.id == exercise.id }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {

                                        if (isSelected) {
                                            selectedExercises.removeIf {
                                                it.exercise.id == exercise.id
                                            }
                                        } else {
                                            selectedExercises.add(
                                                ExerciseConfig(exercise)
                                            )
                                        }
                                    }
                                    .padding(8.dp)
                            ) {

                                Text(exercise.name)

                                if (isSelected) {

                                    val config =
                                        selectedExercises.first {
                                            it.exercise.id == exercise.id
                                        }

                                    Spacer(Modifier.height(8.dp))

                                    // SETS
                                    OutlinedTextField(
                                        value = config.sets.toString(),
                                        onValueChange = {
                                            config.sets = it.toIntOrNull() ?: 0
                                        },
                                        label = { Text("Sets") }
                                    )

                                    Spacer(Modifier.height(6.dp))

                                    // REPS
                                    OutlinedTextField(
                                        value = config.reps.toString(),
                                        onValueChange = {
                                            config.reps = it.toIntOrNull() ?: 0
                                        },
                                        label = { Text("Reps") }
                                    )

                                    // WEIGHT
                                    if (exercise.category == "Βαράκια" ||
                                        exercise.category == "Όργανα"
                                    ) {

                                        Spacer(Modifier.height(6.dp))

                                        OutlinedTextField(
                                            value = config.weight.toString(),
                                            onValueChange = {
                                                config.weight = it.toIntOrNull() ?: 0
                                            },
                                            label = { Text("Kg") }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}