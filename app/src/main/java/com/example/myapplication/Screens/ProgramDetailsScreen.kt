package com.example.myapplication.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.example.myapplication.data.local.relation.ProgramWithExercises
import com.example.myapplication.data.local.entity.Exercise
import com.example.myapplication.viewmodel.ProgramViewModel

data class EditableExercise(
    val exerciseId: Int,
    val name: String,
    val category: String,
    val sets: Int,
    val reps: Int,
    val weight: Int,
    val isNew: Boolean = false
)

@Composable
fun ProgramDetailsScreen(
    program: ProgramWithExercises,
    exercisesPool: List<Exercise>,
    programViewModel: ProgramViewModel,
    onStart: () -> Unit,
    onBack: () -> Unit
) {

    val localExercises = remember { mutableStateListOf<EditableExercise>() }
    var showAddDialog by remember { mutableStateOf(false) }

    // ================= SYNC =================
    LaunchedEffect(program.program.id) {

        localExercises.clear()

        localExercises.addAll(
            program.exercises.map { ex ->
                EditableExercise(
                    exerciseId = ex.exerciseId,
                    name = ex.name,
                    category = ex.category,
                    sets = ex.sets,
                    reps = ex.reps,
                    weight = ex.weight,
                    isNew = false
                )
            }
        )
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // ================= HEADER =================
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Button(onClick = onBack) {
                Text("Back")
            }

            Button(
                onClick = {
                    programViewModel.deleteProgram(program.program)
                    onBack()
                },
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

        // ================= LIST =================
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {

            itemsIndexed(localExercises) { index, ex ->

                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {

                    Column(Modifier.padding(12.dp)) {

                        Text(ex.name)

                        Row {

                            OutlinedTextField(
                                value = ex.sets.toString(),
                                onValueChange = {
                                    localExercises[index] =
                                        ex.copy(sets = it.toIntOrNull() ?: ex.sets)
                                },
                                label = { Text("Sets") },
                                modifier = Modifier.weight(1f)
                            )

                            Spacer(Modifier.width(8.dp))

                            OutlinedTextField(
                                value = ex.reps.toString(),
                                onValueChange = {
                                    localExercises[index] =
                                        ex.copy(reps = it.toIntOrNull() ?: ex.reps)
                                },
                                label = { Text("Reps") },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // ✅ safer condition
                        if (ex.category != "Body") {
                            OutlinedTextField(
                                value = ex.weight.toString(),
                                onValueChange = {
                                    localExercises[index] =
                                        ex.copy(weight = it.toIntOrNull() ?: ex.weight)
                                },
                                label = { Text("Kg") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Row {
                                Button(onClick = {
                                    localExercises.swap(index, index - 1)
                                }, enabled = index > 0) {
                                    Text("⬆")
                                }

                                Spacer(Modifier.width(4.dp))

                                Button(onClick = {
                                    localExercises.swap(index, index + 1)
                                }, enabled = index < localExercises.lastIndex) {
                                    Text("⬇")
                                }
                            }

                            Button(
                                onClick = {
                                    val removed = localExercises[index]
                                    localExercises.removeAt(index)

                                    programViewModel.deleteExercise(
                                        program.program.id,
                                        removed.exerciseId
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                            ) {
                                Text("X")
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // ================= SAVE =================
        Button(
            onClick = {
                programViewModel.saveAll(
                    program.program.id,
                    localExercises.toList()
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("💾 SAVE ALL CHANGES")
        }

        Spacer(Modifier.height(10.dp))

        Button(
            onClick = onStart,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("START WORKOUT")
        }
    }

    // ================= ADD DIALOG =================
    if (showAddDialog) {

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            confirmButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Close")
                }
            },
            title = { Text("Select Exercise") },
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

                                    if (localExercises.none { it.exerciseId == ex.id }) {
                                        localExercises.add(
                                            EditableExercise(
                                                exerciseId = ex.id,
                                                name = ex.name,
                                                category = ex.category,
                                                sets = 3,
                                                reps = 10,
                                                weight = 0,
                                                isNew = true
                                            )
                                        )
                                    }

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

// ================= HELPER =================
fun <T> MutableList<T>.swap(i: Int, j: Int) {
    val tmp = this[i]
    this[i] = this[j]
    this[j] = tmp
}