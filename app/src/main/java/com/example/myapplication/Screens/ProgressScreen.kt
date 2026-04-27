package com.example.myapplication.screens

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.viewmodel.ProgressViewModel
import com.example.myapplication.viewmodel.ProgramViewModel
import com.example.myapplication.viewmodel.ExerciseViewModel
import androidx.compose.ui.unit.Dp

@Composable
fun ProgressScreen() {

    val progressViewModel: ProgressViewModel = viewModel()
    val programViewModel: ProgramViewModel = viewModel()
    val exerciseViewModel: ExerciseViewModel = viewModel()

    val programExercises by programViewModel.activeExercises.collectAsState()
    val exercises by exerciseViewModel.exercises.collectAsState(initial = emptyList())

    // 🔥 ΦΟΡΤΩΣΗ
    LaunchedEffect(Unit) {
        programViewModel.loadProgramExercises(1) // βάλε σωστό id μετά
    }

    val muscleLoads = remember(programExercises, exercises) {
        progressViewModel.calculateMuscleLoad(programExercises, exercises)
    }

    Scaffold { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("Progress", style = MaterialTheme.typography.headlineMedium)

            Spacer(Modifier.height(16.dp))

            // ================= BODY =================
            BodyHeatmap(muscleLoads, progressViewModel)

            Spacer(Modifier.height(20.dp))

            // ================= INFO =================
            Card {
                Column(Modifier.padding(16.dp)) {
                    Text("Weekly Progress")
                    Text("Workouts: 4")
                    Text("Minutes: 180")
                    Text("Calories: 1200")
                }
            }
        }
    }
}@Composable
fun BodyHeatmap(
    muscleLoads: Map<String, Float>,
    vm: ProgressViewModel
) {
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(500.dp)
    ) {

        val w = size.width
        val h = size.height

        fun c(m: String) = vm.getMuscleColor(muscleLoads[m] ?: 0f)

        // ===== HEAD =====
        drawCircle(
            color = Color.LightGray,
            radius = w * 0.08f,
            center = Offset(w / 2, h * 0.1f)
        )

        // ===== CHEST =====
        drawRoundRect(
            color = c("Chest"),
            topLeft = Offset(w * 0.3f, h * 0.18f),
            size = Size(w * 0.4f, h * 0.15f),
            cornerRadius = CornerRadius(20f, 20f)
        )

        // ===== ABS =====
        drawRoundRect(
            color = c("Abs"),
            topLeft = Offset(w * 0.35f, h * 0.34f),
            size = Size(w * 0.3f, h * 0.2f),
            cornerRadius = CornerRadius(16f, 16f)
        )

        // ===== ARMS =====
        drawRoundRect(
            color = c("Biceps"),
            topLeft = Offset(w * 0.15f, h * 0.2f),
            size = Size(w * 0.12f, h * 0.3f),
            cornerRadius = CornerRadius(30f, 30f)
        )

        drawRoundRect(
            color = c("Biceps"),
            topLeft = Offset(w * 0.73f, h * 0.2f),
            size = Size(w * 0.12f, h * 0.3f),
            cornerRadius = CornerRadius(30f, 30f)
        )

        // ===== LEGS =====
        drawRoundRect(
            color = c("Quads"),
            topLeft = Offset(w * 0.35f, h * 0.55f),
            size = Size(w * 0.12f, h * 0.35f),
            cornerRadius = CornerRadius(25f, 25f)
        )

        drawRoundRect(
            color = c("Quads"),
            topLeft = Offset(w * 0.53f, h * 0.55f),
            size = Size(w * 0.12f, h * 0.35f),
            cornerRadius = CornerRadius(25f, 25f)
        )
    }
}
@Composable
fun MuscleBox(name: String, color: Color, width: Dp) {

    Box(
        modifier = Modifier
            .width(width)
            .height(40.dp)
            .background(color, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(name, color = Color.Black)
    }
}