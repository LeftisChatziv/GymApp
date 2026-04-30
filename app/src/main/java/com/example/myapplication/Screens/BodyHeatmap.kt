package com.example.myapplication.Screens

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.FilenameUtils.normalize
import java.text.Normalizer.normalize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.viewmodel.ProgressViewModel
import com.example.myapplication.viewmodel.ProgramViewModel
import com.example.myapplication.viewmodel.ExerciseViewModel

@Composable
fun BodyHeatmap(
    muscleLoads: Map<String, Float>,
    vm: ProgressViewModel
) {

    fun colorFor(muscle: String): Color {
        val key = normalize(muscle)
        return vm.getMuscleColor(muscleLoads[key] ?: 0f)
    }

    val rows = listOf(
        listOf("Ώμοι"),
        listOf("Στήθος"),
        listOf("Δικέφαλα", "Τρικέφαλα"),
        listOf("Κορμός"),
        listOf("Πλάτη"),
        listOf("Τραπεζοειδής"),
        listOf("Πόδια", "Γλουτοί")
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        // head
        Box(
            Modifier
                .size(60.dp)
                .background(Color.LightGray, RoundedCornerShape(50))
        )

        Spacer(Modifier.height(10.dp))

        rows.forEach { row ->

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {

                row.forEachIndexed { index, muscle ->

                    Muscle(
                        name = muscle,
                        color = colorFor(muscle),
                        width = if (row.size == 1) 140.dp else 90.dp
                    )

                    if (index != row.lastIndex) {
                        Spacer(Modifier.width(6.dp))
                    }
                }
            }
        }
    }
}
fun getMuscleColor(load: Float): Color {
    return when {
        load <= 0f -> Color(0xFFE0E0E0)
        load < 5000f -> Color(0xFFFF6B6B)
        load < 15000f -> Color(0xFFFFD93D)
        load < 30000f -> Color(0xFF6BCB77)
        else -> Color(0xFF4D96FF)
    }
}