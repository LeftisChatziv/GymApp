package com.example.myapplication.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.local.relation.ProgramWithExercises

@Composable
fun ProgramDetailsScreen(
    program: ProgramWithExercises,
    onStart: () -> Unit,
    onBack: () -> Unit
) {

    Column(Modifier.padding(16.dp)) {

        Button(onClick = onBack) {
            Text("Back")
        }

        Text(program.program.name, style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(8.dp))

        Text("Days: ${program.program.difficulty}")

        Spacer(Modifier.height(16.dp))

        program.exercises.forEach {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
            ) {
                Text(it.name, modifier = Modifier.padding(12.dp))
            }
        }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = onStart,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("START WORKOUT")
        }
    }
}