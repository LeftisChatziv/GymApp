package com.example.myapplication.Screens

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import com.example.myapplication.data.local.relation.ProgramWithExercises
import com.example.myapplication.viewmodel.WorkoutViewModel

@Composable
fun WorkoutScreen(
    program: ProgramWithExercises,
    onGoToProgress: () -> Unit
) {

    val workoutViewModel: WorkoutViewModel = viewModel()

    val exercises = program.exercises

    var index by remember { mutableStateOf(0) }
    var currentTime by remember { mutableStateOf(0) }
    var totalTime by remember { mutableStateOf(0) }

    val exerciseTimes = remember {
        mutableStateListOf<Triple<String, Int, String>>()
    }

    val isFinished = index >= exercises.size

    // ================= TIMER =================
    LaunchedEffect(index) {

        currentTime = 0

        if (isFinished) return@LaunchedEffect

        while (index < exercises.size) {
            delay(1000)
            currentTime++
        }
    }

    // ================= END SCREEN =================
    if (isFinished) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Workout Completed 🎉",
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Total Time: ${formatTime(totalTime)}",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Exercise Breakdown",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(10.dp))

            exerciseTimes.forEach { (name, time, info) ->
                Text("• $name ($info) → ${formatTime(time)}")
            }

            Spacer(Modifier.height(30.dp))

            Button(
                onClick = onGoToProgress,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("📊 Go to Progress")
            }
        }

        return
    }

    val current = exercises[index]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = current.name,
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(Modifier.height(8.dp))

        // ================= INFO =================
        Card(modifier = Modifier.fillMaxWidth()) {

            Column(Modifier.padding(16.dp)) {

                Text("Workout Info", style = MaterialTheme.typography.titleMedium)

                Spacer(Modifier.height(8.dp))

                Text("Sets: ${current.sets}")
                Text("Reps: ${current.reps}")

                if (current.weight > 0) {
                    Text("Weight: ${current.weight}kg")
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // ================= TIMER =================
        Card(modifier = Modifier.fillMaxWidth()) {

            Column(Modifier.padding(16.dp)) {

                Text("Time", style = MaterialTheme.typography.titleMedium)

                Spacer(Modifier.height(8.dp))

                Text(
                    text = formatTime(currentTime),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // ================= COMPLETE =================
        Button(
            onClick = {

                val sets = current.sets
                val reps = current.reps
                val weight = current.weight

                val info = "Sets: $sets, Reps: $reps" +
                        if (weight > 0) ", Weight: ${weight}kg" else ""

                exerciseTimes.add(
                    Triple(current.name, currentTime, info)
                )

                totalTime += currentTime

                // 🔥 SAVE TO DB (FIXED SAFETY)
                workoutViewModel.saveWorkoutHistory(
                    programId = program.program.id,
                    programName = program.program.name,
                    exerciseName = current.name,
                    sets = sets,
                    reps = reps,
                    weight = weight,
                    duration = currentTime.coerceAtLeast(1),
                    totalExercises = exercises.size,
                    completedExercises = index + 1
                )

                index++
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Complete Exercise")
        }
    }
}

// ================= FORMAT =================
fun formatTime(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return "%02d:%02d".format(mins, secs)
}