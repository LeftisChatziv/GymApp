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
    var selectedFilter by remember { mutableStateOf("Όλα") }

    val filters = listOf("Όλα", "Σώμα", "Βαράκια", "Όργανα")

    val exercises by viewModel.exercises.collectAsState(initial = emptyList())

    // 🔥 FIX: search + category filter
    val filteredExercises = remember(exercises, search, selectedFilter) {
        exercises.filter { exercise ->

            val matchesSearch =
                exercise.name.contains(search, ignoreCase = true)

            val matchesCategory =
                selectedFilter == "Όλα" || exercise.category == selectedFilter

            matchesSearch && matchesCategory
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
            color = colors.onBackground,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            placeholder = { Text("Αναζήτηση άσκησης...") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colors.primary,
                unfocusedBorderColor = colors.onSurfaceVariant,
                cursorColor = colors.primary
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 🔥 FILTER CHIPS (WORKING)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

            filters.forEach { filter ->

                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = { Text(filter) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = colors.primary,
                        selectedLabelColor = colors.onPrimary
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔥 LIST
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(filteredExercises) { exercise ->

                ExerciseCard(exercise)
            }
        }
    }
}