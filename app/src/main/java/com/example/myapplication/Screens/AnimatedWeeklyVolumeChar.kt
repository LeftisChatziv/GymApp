package com.example.myapplication.Screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedWeeklyVolumeChart(data: List<Float>) {

    val safeData = if (data.size == 7) data else List(7) { 0f }
    val maxValue = (safeData.maxOrNull() ?: 1f).coerceAtLeast(1f)

    // 🔥 animation progress (0 → 1)
    val transition = rememberInfiniteTransition(label = "chart")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1_000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "progress"
    )

    Column {

        Text("Weekly Volume (Animated)", style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(12.dp))

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) {

            val stepX = size.width / 6f
            val height = size.height

            // axes
            drawLine(Color.Gray, Offset(0f, height), Offset(size.width, height), 3f)
            drawLine(Color.Gray, Offset(0f, 0f), Offset(0f, height), 3f)

            // line
            for (i in 0 until safeData.lastIndex) {

                val v1 = safeData[i] * progress
                val v2 = safeData[i + 1] * progress

                val x1 = i * stepX
                val y1 = height - (v1 / maxValue) * height

                val x2 = (i + 1) * stepX
                val y2 = height - (v2 / maxValue) * height

                drawLine(
                    color = Color(0xFF4D96FF),
                    start = Offset(x1, y1),
                    end = Offset(x2, y2),
                    strokeWidth = 5f
                )
            }
        }
    }
}