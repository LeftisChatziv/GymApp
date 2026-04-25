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

    var index by remember { mutableStateOf(0) }

    val exercises = program.exercises

    // 🔥 χρόνος τρέχουσας άσκησης
    var currentTime by remember { mutableStateOf(0) }

    // 🔥 συνολικός χρόνος
    var totalTime by remember { mutableStateOf(0) }

    // 🔥 λίστα με χρόνους ανά άσκηση
    val exerciseTimes = remember { mutableStateListOf<Pair<String, Int>>() }

    // 🔥 TIMER EFFECT (τρέχει κάθε δευτερόλεπτο)
    LaunchedEffect(index) {
        currentTime = 0

        while (true) {
            delay(1000)
            currentTime++
        }
    }

    // ✅ ΤΕΛΟΣ WORKOUT
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

            Text("Total Time: ${formatTime(totalTime)}")

            Spacer(Modifier.height(20.dp))

            Text("Details:", style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(10.dp))

            exerciseTimes.forEach { (name, time) ->
                Text("$name - ${formatTime(time)}")
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

        Spacer(modifier = Modifier.height(20.dp))

        // 🔥 TIMER DISPLAY
        Text(
            text = formatTime(currentTime),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                // 🔥 αποθήκευση χρόνου άσκησης
                exerciseTimes.add(current.name to currentTime)

                totalTime += currentTime

                index++
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Complete Exercise")
        }
    }
}

// 🔥 helper για μορφή χρόνου
fun formatTime(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return "%02d:%02d".format(mins, secs)
}