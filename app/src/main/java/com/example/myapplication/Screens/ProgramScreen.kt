package com.example.myapplication.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.local.entity.Exercise
import com.example.myapplication.data.local.relation.ProgramWithExercises
import com.example.myapplication.viewmodel.*
import com.example.myapplication.Screens.WorkoutScreen

data class ExerciseConfig(
    val exercise: Exercise,
    val sets: Int,
    val reps: Int,
    val weight: Int
)

fun mapDay(day: String): String = when (day) {
    "Mon" -> "Δε"
    "Tue" -> "Τρ"
    "Wed" -> "Τε"
    "Thu" -> "Πε"
    "Fri" -> "Πα"
    "Sat" -> "Σα"
    "Sun" -> "Κυ"
    else -> day
}

@Composable
fun ProgramScreen(
    exerciseViewModel: ExerciseViewModel = viewModel(),
    programViewModel: ProgramViewModel = viewModel()
) {

    val exercises by exerciseViewModel.exercises.collectAsState(initial = emptyList())
    val programs by programViewModel.programs.collectAsState(initial = emptyList())

    var showDialog by remember { mutableStateOf(false) }
    var selectedProgram by remember { mutableStateOf<ProgramWithExercises?>(null) }
    var startWorkout by remember { mutableStateOf(false) }

    var title by rememberSaveable { mutableStateOf("") }
    val selectedDays = remember { mutableStateListOf<String>() }

    val selectedExercises = remember {
        mutableStateMapOf<Int, ExerciseConfig>()
    }

    // =====================
    // PROGRAM DETAILS SCREEN
    // =====================
    selectedProgram?.let { program ->

        if (startWorkout) {
            WorkoutScreen(program)
            return
        }

        ProgramDetailsScreen(
            program = program,
            exercisesPool = exercises,

            onStart = { startWorkout = true },

            onBack = {
                selectedProgram = null
                startWorkout = false
                programViewModel.loadPrograms()
            },

            onDeleteProgram = {
                programViewModel.deleteProgram(program.program)
                selectedProgram = null
            },

            onSaveAll = { updatedList ->
                // 🔥 εδώ θα το συνδέσουμε σωστά με Room save later
                println("SAVE ALL: $updatedList")
            }
        )

        return
    }

    // =====================
    // MAIN SCREEN
    // =====================
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

                    val prettyDays = program.program.days
                        .split(",")
                        .filter { it.isNotBlank() }
                        .joinToString(" • ") { mapDay(it) }

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
                            Text("Days: $prettyDays")
                        }
                    }
                }
            }
        }
    }

    // =====================
    // CREATE PROGRAM DIALOG
    // =====================
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

                    programViewModel.createProgram(
                        title = title,
                        days = selectedDays.toList(),
                        exercises = plan
                    )

                    // RESET
                    showDialog = false
                    title = ""
                    selectedDays.clear()
                    selectedExercises.clear()
                    programViewModel.loadPrograms()

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
                                label = { Text(mapDay(day)) }
                            )
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    LazyColumn(Modifier.height(220.dp)) {

                        items(exercises) { exercise ->

                            val config = selectedExercises[exercise.id]

                            Column(
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