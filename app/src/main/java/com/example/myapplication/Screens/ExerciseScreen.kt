package com.example.myapplication.Screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.viewmodel.ExerciseViewModel

@Composable
fun ExercisesScreen(
    viewModel: ExerciseViewModel = viewModel()
) {

    var search by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Όλα") }
    var selectedDifficulty by remember { mutableStateOf("Όλα") }

    val categories = listOf("Όλα", "Σώμα", "Βαράκια", "Όργανα")
    val difficulties = listOf("Όλα", "Easy", "Hard")

    val exercises by viewModel.exercises.collectAsState(initial = emptyList())

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

    val configuration = LocalConfiguration.current
    val isLandscape =
        configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .verticalScroll(rememberScrollState()) // 🔥 FULL SCREEN SCROLL
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

        // 🔥 DIFFICULTY FILTER
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

        // =======================
        // 📱 LIST / GRID CONTENT
        // =======================

        if (isLandscape) {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(800.dp) // allows outer scroll to work
            ) {
                items(filteredExercises) { exercise ->
                    ExerciseCard(exercise)
                }
            }

        } else {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(800.dp)
            ) {
                items(filteredExercises) { exercise ->
                    ExerciseCard(exercise)
                }
            }
        }
    }
}