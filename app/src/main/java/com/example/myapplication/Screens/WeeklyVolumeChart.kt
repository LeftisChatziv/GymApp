package com.example.myapplication.Screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun WeeklyVolumeChart(data: List<Float>) {

    val safeData =
        if (data.size == 7) data
        else (data + List(7 - data.size) { 0f }).take(7)

    val maxY = 1000f   // 🔥 fixed scale όπως ζήτησες

    Column {

        Text("Weekly Volume", style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(12.dp))

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        ) {

            val w = size.width
            val h = size.height
            val step = w / 6f

            // ================= GRID =================
            val gridLines = 5
            for (i in 0..gridLines) {
                val y = h - (i / gridLines.toFloat()) * h
                drawLine(
                    color = Color.LightGray.copy(alpha = 0.3f),
                    start = Offset(0f, y),
                    end = Offset(w, y),
                    strokeWidth = 2f
                )
            }

            // ================= AXIS =================
            drawLine(Color.Gray, Offset(0f, h), Offset(w, h), 3f)
            drawLine(Color.Gray, Offset(0f, 0f), Offset(0f, h), 3f)

            // ================= SMOOTH PATH =================
            val points = safeData.mapIndexed { i, v ->
                val x = i * step
                val y = h - (v / maxY) * h
                Offset(x, y)
            }

            val path = Path()

            for (i in points.indices) {

                val p = points[i]

                if (i == 0) {
                    path.moveTo(p.x, p.y)
                } else {
                    val prev = points[i - 1]

                    val midX = (prev.x + p.x) / 2

                    path.quadraticBezierTo(
                        prev.x, prev.y,
                        midX, (prev.y + p.y) / 2
                    )
                }
            }

            // ================= GRADIENT LINE =================
            drawPath(
                path = path,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1E88E5),
                        Color(0xFF42A5F5),
                        Color(0xFF90CAF9)
                    )
                ),
                style = Stroke(width = 6f, cap = StrokeCap.Round)
            )

            // ================= DOTS =================
            points.forEach { p ->

                drawCircle(
                    color = Color.White,
                    radius = 10f,
                    center = p
                )

                drawCircle(
                    color = Color(0xFF1E88E5),
                    radius = 6f,
                    center = p
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                .forEach {
                    Text(it, style = MaterialTheme.typography.bodySmall)
                }
        }

        Spacer(Modifier.height(6.dp))

        Text(
            "Max scale: 100.000",
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray
        )
    }
}