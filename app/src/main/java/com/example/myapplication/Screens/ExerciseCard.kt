package com.example.myapplication.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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

            Text(
                text = exercise.name,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(exercise.description)

            Spacer(modifier = Modifier.height(12.dp))

            exercise.muscleGroups.forEach { muscle ->

                Column(modifier = Modifier.fillMaxWidth()) {

                    // 🔹 Row: όνομα + %
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(muscle.muscle)
                        Text("${muscle.percentage}%")
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // 🔥 Progress Bar "δοχείο"
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(50)
                            )
                    ) {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth(muscle.percentage / 100f)
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