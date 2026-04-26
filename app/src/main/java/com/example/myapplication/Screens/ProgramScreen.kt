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
import com.example.myapplication.data.local.entity.Exercise
import com.example.myapplication.data.local.relation.ProgramWithExercises
import com.example.myapplication.data.local.relation.ProgramExerciseItem
import com.example.myapplication.viewmodel.ProgramViewModel
import com.example.myapplication.viewmodel.ExerciseViewModel
import com.example.myapplication.viewmodel.ExercisePlan
import com.example.myapplication.Screens.ProgramDetailsScreen

data class ExerciseConfig(
    val exercise: Exercise,
    val sets: Int,
    val reps: Int,
    val weight: Int
)

@Composable
fun ProgramScreen() {

    val viewModel: ProgramViewModel = viewModel()
    val exerciseViewModel: ExerciseViewModel = viewModel()

    val programs by viewModel.programs.collectAsState()
    val exercises by exerciseViewModel.exercises.collectAsState(initial = emptyList())

    var selectedProgram by remember { mutableStateOf<ProgramWithExercises?>(null) }

    var showDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }

    val selectedDays = remember { mutableStateListOf<String>() }
    val selectedExercises = remember { mutableStateMapOf<Int, ExerciseConfig>() }

    // ================= DETAILS =================
    selectedProgram?.let { program ->

        ProgramDetailsScreen(
            program = program,
            exercisesPool = exercises,

            onStart = {},

            onBack = {
                selectedProgram = null
            },

            onDeleteProgram = {
                viewModel.deleteProgram(program.program)
                selectedProgram = null
            },

            onSaveAll = { programId, list: List<EditableExercise> ->

                viewModel.saveAll(programId, list)
            },

            onDeleteExercise = { programId, exerciseId ->
                viewModel.deleteExercise(programId, exerciseId)
            }
        )

        return
    }

    // ================= MAIN =================
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
                                selectedProgram = program
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

    // ================= CREATE =================
    if (showDialog) {

        AlertDialog(
            onDismissRequest = { showDialog = false },

            confirmButton = {

                TextButton(onClick = {

                    val plan = selectedExercises.values
                        .toList()
                        .mapIndexed { index, it ->
                            ExercisePlan(
                                exerciseId = it.exercise.id,
                                sets = it.sets,
                                reps = it.reps,
                                weight = it.weight,
                                position = index
                            )
                        }

                    viewModel.createProgram(
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

                    LazyColumn(Modifier.height(200.dp)) {

                        items(exercises) { exercise ->

                            val config = selectedExercises[exercise.id]

                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (config != null)
                                            selectedExercises.remove(exercise.id)
                                        else
                                            selectedExercises[exercise.id] =
                                                ExerciseConfig(exercise, 3, 10, 0)
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