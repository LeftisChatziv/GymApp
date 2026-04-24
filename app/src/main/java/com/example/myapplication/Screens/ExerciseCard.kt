package com.example.myapplication.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.local.entity.Exercise

@Composable
fun ExerciseCard(exercise: Exercise) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            // 🔥 TITLE
            Text(
                text = exercise.name,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            // 🔥 CATEGORY CHIP
            AssistChip(
                onClick = {},
                label = {
                    Text(text = exercise.category)
                }
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(exercise.description)

            Spacer(modifier = Modifier.height(12.dp))

            // 🔥 SAFE LIST HANDLING
            val muscles = exercise.muscleGroups ?: emptyList()

            if (muscles.isEmpty()) {
                Text(
                    text = "Δεν υπάρχουν δεδομένα μυϊκής ενεργοποίησης",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {

                muscles.forEach { muscle ->

                    val progress = (muscle.percentage / 100f).coerceIn(0f, 1f)

                    Column(modifier = Modifier.fillMaxWidth()) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(muscle.muscle)
                            Text("${muscle.percentage}%")
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // 🔥 FULL WIDTH BACKGROUND BAR
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(50)
                                )
                        ) {

                            // 🔥 GREEN PROGRESS
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(progress)
                                    .fillMaxHeight()
                                    .background(
                                        color = Color(0xFF4CAF50),
                                        shape = RoundedCornerShape(50)
                                    )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}