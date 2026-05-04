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

    val colorFor: (String) -> Color = { muscle ->
        vm.getMuscleColor(muscleLoads[muscle] ?: 0f)
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

                    val load = muscleLoads[muscle] ?: 0f

                    Muscle(
                        name = "$muscle\n${load.toInt()}",
                        color = vm.getMuscleColor(load),
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