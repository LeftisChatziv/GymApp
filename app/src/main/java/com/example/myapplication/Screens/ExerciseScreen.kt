package com.example.myapplication.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.viewmodel.ExerciseViewModel
@Composable
fun ExercisesScreen(
    viewModel: ExerciseViewModel = viewModel()
) {

    var search by remember { mutableStateOf("") }

    // 🔥 2 ΑΝΕΞΑΡΤΗΤΑ FILTERS
    var selectedCategory by remember { mutableStateOf("Όλα") }
    var selectedDifficulty by remember { mutableStateOf("Όλα") }

    val categories = listOf("Όλα", "Σώμα", "Βαράκια", "Όργανα")
    val difficulties = listOf("Όλα", "Easy", "Hard")

    val exercises by viewModel.exercises.collectAsState(initial = emptyList())

    // 🔥 COMBINED FILTER
    val filteredExercises = remember(
        exercises,
        search,
        selectedCategory,
        selectedDifficulty
    ) {
        exercises.filter { exercise ->

            val matchesSearch =
                exercise.name.contains(search, ignoreCase = true)

            val matchesCategory =
                selectedCategory == "Όλα" ||
                        exercise.category == selectedCategory

            val matchesDifficulty =
                selectedDifficulty == "Όλα" ||
                        exercise.difficulty == selectedDifficulty

            matchesSearch && matchesCategory && matchesDifficulty
        }
    }

    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(16.dp)
    ) {

        Text(
            text = "Ασκήσεις",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 🔍 SEARCH
        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            placeholder = { Text("Αναζήτηση...") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 🏷️ CATEGORY FILTER
        Text("Κατηγορία")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            categories.forEach { cat ->
                FilterChip(
                    selected = selectedCategory == cat,
                    onClick = { selectedCategory = cat },
                    label = { Text(cat) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 🔥 DIFFICULTY FILTER (ΞΕΧΩΡΙΣΤΟ)
        Text("Δυσκολία")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            difficulties.forEach { diff ->
                FilterChip(
                    selected = selectedDifficulty == diff,
                    onClick = { selectedDifficulty = diff },
                    label = { Text(diff) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredExercises) { exercise ->
                ExerciseCard(exercise)
            }
        }
    }
}