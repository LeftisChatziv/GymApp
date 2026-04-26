package com.example.myapplication.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

data class EditableExercise(
    val exerciseId: Int,
    val name: String,
    val category: String = "",
    val sets: Int,
    val reps: Int,
    val weight: Int
)

@Composable
fun ProgramDetailsScreen(
    program: com.example.myapplication.data.local.relation.ProgramWithExercises,
    exercisesPool: List<com.example.myapplication.data.local.entity.Exercise>,
    onStart: () -> Unit,
    onBack: () -> Unit,
    onDeleteProgram: () -> Unit,
    onSaveAll: (List<EditableExercise>) -> Unit
) {

    val scroll = rememberScrollState()

    val localExercises: SnapshotStateList<EditableExercise> =
        remember(program.program.id) {
            program.exercises
                .map {
                    EditableExercise(
                        exerciseId = it.exerciseId,
                        name = it.name,
                        category = it.category,
                        sets = it.sets,
                        reps = it.reps,
                        weight = it.weight
                    )
                }
                .toMutableStateList()
        }

    var showAddDialog by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scroll)
    ) {

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onBack) { Text("Back") }

            Button(
                onClick = onDeleteProgram,
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
            ) {
                Text("Delete")
            }
        }

        Spacer(Modifier.height(8.dp))

        Text(program.program.name, style = MaterialTheme.typography.headlineMedium)
        Text("Days: ${program.program.days}")

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { showAddDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("➕ Add Exercise")
        }

        Spacer(Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.heightIn(max = 500.dp)
        ) {

            itemsIndexed(
                items = localExercises,
                key = { index, item -> "${item.exerciseId}-$index" } // ✔ SAFE KEY
            ) { index, ex ->

                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {

                    Column(Modifier.padding(12.dp)) {

                        Text(ex.name, style = MaterialTheme.typography.titleMedium)

                        fun update(updated: EditableExercise) {
                            localExercises[index] = updated.toMutableCopy()
                        }

                        Row {

                            OutlinedTextField(
                                value = ex.sets.toString(),
                                onValueChange = {
                                    update(ex.copy(sets = it.toIntOrNull() ?: 0))
                                },
                                label = { Text("Sets") },
                                modifier = Modifier.weight(1f)
                            )

                            Spacer(Modifier.width(8.dp))

                            OutlinedTextField(
                                value = ex.reps.toString(),
                                onValueChange = {
                                    update(ex.copy(reps = it.toIntOrNull() ?: 0))
                                },
                                label = { Text("Reps") },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        if (ex.category.lowercase() != "body") {

                            OutlinedTextField(
                                value = ex.weight.toString(),
                                onValueChange = {
                                    update(ex.copy(weight = it.toIntOrNull() ?: 0))
                                },
                                label = { Text("Kg") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Text(
                                "Bodyweight exercise (no weight needed)",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Row {

                                Button(onClick = {
                                    if (index > 0) {
                                        localExercises.swap(index, index - 1)
                                    }
                                }) { Text("⬆") }

                                Spacer(Modifier.width(4.dp))

                                Button(onClick = {
                                    if (index < localExercises.lastIndex) {
                                        localExercises.swap(index, index + 1)
                                    }
                                }) { Text("⬇") }
                            }

                            Button(
                                onClick = { localExercises.removeAt(index) },
                                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                            ) {
                                Text("X")
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = { onSaveAll(localExercises.map { it.copy() }) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("💾 SAVE ALL CHANGES")
        }

        Spacer(Modifier.height(10.dp))

        Button(onClick = onStart, modifier = Modifier.fillMaxWidth()) {
            Text("START WORKOUT")
        }
    }

    if (showAddDialog) {

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            confirmButton = {},

            text = {

                Column(
                    modifier = Modifier
                        .heightIn(max = 300.dp)
                        .verticalScroll(rememberScrollState())
                ) {

                    exercisesPool.forEach { ex ->

                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable {

                                    localExercises.add(
                                        EditableExercise(
                                            exerciseId = ex.id,
                                            name = ex.name,
                                            category = ex.category,
                                            sets = 3,
                                            reps = 10,
                                            weight = 0
                                        )
                                    )

                                    showAddDialog = false
                                }
                                .padding(8.dp)
                        ) {
                            Text(ex.name)
                        }
                    }
                }
            }
        )
    }
}

// SAFE SWAP
private fun <T> MutableList<T>.swap(i: Int, j: Int) {
    val tmp = this[i]
    this[i] = this[j]
    this[j] = tmp
}

// SAFE COPY EXTENSION (helps recomposition)
private fun EditableExercise.toMutableCopy() = this.copy()