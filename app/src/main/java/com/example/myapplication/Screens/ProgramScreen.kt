package com.example.myapplication.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.local.entity.*
import com.example.myapplication.data.local.relation.ProgramWithExercises
import com.example.myapplication.viewmodel.ProgramViewModel
import com.example.myapplication.viewmodel.ExerciseViewModel

@Composable
fun ProgramScreen(
    onOpenProgram: (ProgramWithExercises) -> Unit
) {

    val programViewModel: ProgramViewModel = viewModel()
    val exerciseViewModel: ExerciseViewModel = viewModel()

    val programs by programViewModel.programs.collectAsState()
    val exercises by exerciseViewModel.exercises.collectAsState(initial = emptyList())

    var showDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }

    val selectedDays = remember { mutableStateListOf<String>() }

    // ✅ μόνο UI state (ΟΧΙ DB object)
    data class TempExercise(
        val exerciseId: Int,
        val sets: Int,
        val reps: Int,
        val weight: Int
    )

    val selectedExercises = remember {
        mutableStateMapOf<Int, TempExercise>()
    }

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

            Text("Programs", style = MaterialTheme.typography.headlineMedium)

            Spacer(Modifier.height(12.dp))

            LazyColumn {
                items(programs) { program ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                onOpenProgram(program)
                            }
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(program.program.name)
                            Text("Exercises: ${program.exercises.size}")
                        }
                    }
                }
            }
        }
    }

    // ================= CREATE DIALOG =================
    if (showDialog) {

        AlertDialog(
            onDismissRequest = { showDialog = false },

            confirmButton = {

                TextButton(onClick = {

                    // 🔥 FIX: convert UI → DB model σωστά
                    val crossRefs = selectedExercises.values.mapIndexed { index, item ->
                        ProgramExerciseCrossRef(
                            programId = 0, // ❗ αγνοείται, μπαίνει στο ViewModel
                            exerciseId = item.exerciseId,
                            sets = item.sets,
                            reps = item.reps,
                            weight = item.weight,
                            position = index
                        )
                    }

                    programViewModel.createProgram(
                        title = title,
                        days = selectedDays.toList(),
                        exercises = crossRefs
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

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Program Title") }
                    )

                    Spacer(Modifier.height(10.dp))

                    Row {
                        listOf("Mon","Tue","Wed","Thu","Fri","Sat","Sun").forEach { day ->

                            val selected = selectedDays.contains(day)

                            FilterChip(
                                selected = selected,
                                onClick = {
                                    if (selected) selectedDays.remove(day)
                                    else selectedDays.add(day)
                                },
                                label = { Text(day) }
                            )
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    LazyColumn(
                        modifier = Modifier.height(200.dp)
                    ) {

                        items(exercises) { exercise ->

                            val selected = selectedExercises[exercise.id]

                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (selected != null) {
                                            selectedExercises.remove(exercise.id)
                                        } else {
                                            selectedExercises[exercise.id] =
                                                TempExercise(
                                                    exerciseId = exercise.id,
                                                    sets = 3,
                                                    reps = 10,
                                                    weight = 0
                                                )
                                        }
                                    }
                                    .padding(8.dp)
                            ) {
                                Text(exercise.name)
                            }
                        }
                    }
                }
            }
        )
    }
}