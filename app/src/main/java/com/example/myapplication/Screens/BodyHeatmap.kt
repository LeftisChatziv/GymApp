package com.example.myapplication.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BodyHeatmap(
    muscleLoads: Map<String, Int>
) {

    val loads = remember(muscleLoads) {
        muscleLoads.toMap()
    }

    fun color(load: Int): Color {
        return when {
            load <= 0 -> Color(0xFFE0E0E0)
            load < 5000 -> Color(0xFFFF6B6B)
            load < 15000 -> Color(0xFFFFD93D)
            load < 30000 -> Color(0xFF6BCB77)
            else -> Color(0xFF4D96FF)
        }
    }

    val rows = listOf(
        listOf("ώμοι"),
        listOf("στήθος"),
        listOf("δικέφαλα", "τρικέφαλα"),
        listOf("πήχεις"),
        listOf("κορμός"),
        listOf("πλάτη"),
        listOf("τραπεζοειδής"),
        listOf("πόδια", "γλουτοί")
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ================= DEBUG =================

        Text(
            text = "Muscle keys: ${loads.keys.joinToString()}",
            style = MaterialTheme.typography.labelSmall
        )

        Text(
            text = "SIZE: ${muscleLoads.size}",
            style = MaterialTheme.typography.labelSmall
        )

        Spacer(modifier = Modifier.height(10.dp))

        // ================= HEATMAP =================

        rows.forEach { row ->

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {

                row.forEachIndexed { index, muscle ->

                    val load = loads[muscle] ?: 0

                    Box(
                        modifier = Modifier
                            .width(
                                if (row.size == 1) 160.dp
                                else 90.dp
                            )
                            .height(50.dp)
                            .background(
                                color = color(load),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "$muscle\n$load"
                        )
                    }

                    if (index != row.lastIndex) {
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                }
            }
        }
    }
}