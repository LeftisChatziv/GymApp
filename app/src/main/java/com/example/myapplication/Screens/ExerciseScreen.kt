package com.example.myapplication.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ExercisesScreen() {

    var search by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Όλα") }

    val filters = listOf("Όλα", "Σώμα", "Βαράκια", "Όργανα")

    val exercises = listOf(
        "Push-ups (Κάμψεις)",
        "Bench Press",
        "Pull-ups (Πουλιά)",
        "Squats (Καθίσματα)"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F7FB))
            .padding(16.dp)
    ) {

        Text(
            text = "Ασκήσεις",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        // SEARCH BAR
        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            placeholder = { Text("Αναζήτηση άσκησης...") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // FILTER CHIPS
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            filters.forEach { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = { Text(filter) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // LIST
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(exercises) { exercise ->
                ExerciseCard(exercise)
            }
        }
    }
}
@Composable
fun ExerciseCard(title: String) {

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(title, fontWeight = FontWeight.Bold)

                AssistChip(
                    onClick = {},
                    label = { Text("Σώμα") }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Στήθος", fontWeight = FontWeight.Medium)

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = 0.6f,
                modifier = Modifier.fillMaxWidth()
            )

            Text("Στήθος 60%", style = MaterialTheme.typography.labelSmall)

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = 0.3f,
                modifier = Modifier.fillMaxWidth()
            )

            Text("Τρικέφαλος 30%", style = MaterialTheme.typography.labelSmall)
        }
    }
}