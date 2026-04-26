package com.example.myapplication.Screens

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import com.example.myapplication.data.local.relation.ProgramWithExercises

@Composable
fun WorkoutScreen(program: ProgramWithExercises) {

    val exercises = program.exercises

    var index by remember { mutableStateOf(0) }

    var currentTime by remember { mutableStateOf(0) }
    var totalTime by remember { mutableStateOf(0) }

    val exerciseTimes = remember {
        mutableStateListOf<Triple<String, Int, String>>()
    }

    // 🔥 TIMER (FIXED - stops properly on change)
    LaunchedEffect(index) {

        currentTime = 0

        if (index >= exercises.size) return@LaunchedEffect

        while (true) {
            delay(1000)
            currentTime++

            // safety stop
            if (index >= exercises.size) break
        }
    }

    // 🔥 END SCREEN
    if (index >= exercises.size) {

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
        }

        return
    }

    val current = exercises[index]

    val sets = current.sets
    val reps = current.reps
    val weight = current.weight

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

        // 🔥 INFO CARD
        Card(modifier = Modifier.fillMaxWidth()) {

            Column(Modifier.padding(16.dp)) {

                Text("Workout Info", style = MaterialTheme.typography.titleMedium)

                Spacer(Modifier.height(8.dp))

                Text("Sets: $sets")
                Text("Reps: $reps")

                if (weight > 0) {
                    Text("Weight: ${weight}kg")
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // 🔥 TIMER CARD
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

        Button(
            onClick = {

                val info = buildString {
                    append("Sets: $sets, Reps: $reps")
                    if (weight > 0) {
                        append(", Weight: ${weight}kg")
                    }
                }

                exerciseTimes.add(
                    Triple(current.name, currentTime, info)
                )

                totalTime += currentTime
                index++
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Complete Exercise")
        }
    }
}

// 🔥 time formatter
fun formatTime(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return "%02d:%02d".format(mins, secs)
}