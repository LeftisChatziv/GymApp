package com.example.myapplication.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProgressScreen() {

    Scaffold { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            Text(
                text = "Progress",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card {
                Column(Modifier.padding(16.dp)) {
                    Text("Weekly Progress")
                    Spacer(Modifier.height(8.dp))
                    Text("Workouts: 4")
                    Text("Minutes: 180")
                    Text("Calories: 1200")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card {
                Column(Modifier.padding(16.dp)) {
                    Text("Goal Progress")
                    Spacer(Modifier.height(8.dp))
                    Text("You are 60% to your weekly goal")
                }
            }
        }
    }
}