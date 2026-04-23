package com.example.myapplication.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProgramScreen() {

    Scaffold { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            Text(
                text = "Programs",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card {
                Column(Modifier.padding(16.dp)) {
                    Text("Beginner Program")
                    Spacer(Modifier.height(4.dp))
                    Text("Easy • Full Body • 5 Exercises")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card {
                Column(Modifier.padding(16.dp)) {
                    Text("Advanced Program")
                    Spacer(Modifier.height(4.dp))
                    Text("Hard • Split Training • 5 Exercises")
                }
            }
        }
    }
}