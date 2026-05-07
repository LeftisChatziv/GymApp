package com.example.myapplication.Screens

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import com.example.myapplication.data.local.entity.Exercise
import com.example.myapplication.data.local.relation.ProgramWithExercises
import com.example.myapplication.data.local.relation.ProgramExerciseItem
import com.example.myapplication.viewmodel.WorkoutViewModel
import com.example.myapplication.viewmodel.UserStatsViewModel

// ================= METRICS MODEL =================
data class WorkoutMetrics(
    val totalVolume: Float,
    val muscleLoad: Map<String, Int>,
    val xp: Int
)

// ================= CALC =================
private fun calcBodyWeight(userWeight: Int, percent: Int): Int {
    return (userWeight * percent) / 100
}

// ================= BUILD METRICS =================
fun buildWorkoutMetrics(
    exercises: List<ProgramExerciseItem>,
    exercisePool: List<Exercise>,
    userWeight: Int
): WorkoutMetrics {

    var totalVolume = 0f
    val muscleMap = mutableMapOf<String, Int>()
    var xp = 0

    exercises.forEach { ex ->

        val poolEx = exercisePool.find { it.id == ex.exerciseId }

        val isBody = poolEx?.category?.contains("Σώμα", ignoreCase = true) == true

        val load = if (isBody && poolEx != null) {
            poolEx.muscleGroups.maxOfOrNull {
                calcBodyWeight(userWeight, it.percentage)
            } ?: calcBodyWeight(userWeight, 50)
        } else {
            ex.weight
        }

        totalVolume += (ex.sets * ex.reps * load).toFloat()

        poolEx?.muscleGroups?.forEach { mg ->
            val current = muscleMap[mg.muscle] ?: 0
            muscleMap[mg.muscle] =
                current + calcBodyWeight(userWeight, mg.percentage)
        }

        xp += ((ex.sets * ex.reps * load) / 10f).toInt()
    }

    return WorkoutMetrics(
        totalVolume = totalVolume,
        muscleLoad = muscleMap,
        xp = xp
    )
}

// ================= SCREEN =================
@Composable
fun WorkoutScreen(
    program: ProgramWithExercises,
    exercisePool: List<Exercise>,
    userWeight: Int,
    onGoToProgress: () -> Unit
) {

    val workoutViewModel: WorkoutViewModel = viewModel()
    val userStatsViewModel: UserStatsViewModel = viewModel()

    val exercises = program.exercises

    var index by remember { mutableStateOf(0) }
    var currentTime by remember { mutableStateOf(0) }
    var totalTime by remember { mutableStateOf(0) }

    val uid = remember { "demo" }

    val isFinished = index >= exercises.size

    // ================= TIMER FIX =================
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

        val metrics = buildWorkoutMetrics(
            exercises = exercises,
            exercisePool = exercisePool,
            userWeight = userWeight
        )

        // 🔥 IMPORTANT FIX: SAVE HISTORY + STATS ONLY ONCE
        LaunchedEffect(Unit) {

            userStatsViewModel.completeWorkout(uid, exercises)

            workoutViewModel.saveWorkoutHistory(
                programId = program.program.id,
                programName = program.program.name,
                durationSeconds = totalTime,
                totalExercises = exercises.size,
                completedExercises = exercises.size,
                exercises = exercises
            )
        }

        Column(
            Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                "Workout Completed 🎉",
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(Modifier.height(12.dp))

            Text("XP Gained: ${metrics.xp}")
            Text("Total Volume: ${metrics.totalVolume}")

            Spacer(Modifier.height(16.dp))

            Text("Muscle Activation")

            metrics.muscleLoad.forEach { (muscle, load) ->
                Text("• $muscle → $load")
            }

            Spacer(Modifier.height(24.dp))

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
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(current.name, style = MaterialTheme.typography.headlineLarge)

        Spacer(Modifier.height(8.dp))

        Text("Sets: ${current.sets}")
        Text("Reps: ${current.reps}")
        Text("Weight: ${current.weight}")

        Spacer(Modifier.height(20.dp))

        Text("Time: ${formatTime(currentTime)}")

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {

                totalTime += currentTime
                index++
                currentTime = 0
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Complete Exercise")
        }
    }
}

// ================= FORMAT =================
fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return "%02d:%02d".format(m, s)
}