package com.example.myapplication.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BodyHeatmap(muscleLoads: Map<String, Int>) {

    fun getColor(load: Int): Color {
        return when {
            load <= 500 -> Color(0xFFD32F2F)   // 🔴 λίγο γυμνασμένο
            load <= 2000 -> Color(0xFFFFA000)  // 🟠 μέτριο
            load <= 8000 -> Color(0xFF43A047)  // 🟢 δυνατό
            else -> Color(0xFF1E88E5)         // 🔵 πολύ δυνατό / max activation
        }
    }

    fun load(m: String) = muscleLoads[m] ?: 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        MuscleBox("ώμοι", load("ώμοι"), getColor(load("ώμοι")))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MuscleBox("στήθος", load("στήθος"), getColor(load("στήθος")))
            MuscleBox("πλάτη", load("πλάτη"), getColor(load("πλάτη")))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MuscleBox("δικέφαλα", load("δικέφαλα"), getColor(load("δικέφαλα")))
            MuscleBox("τρικέφαλα", load("τρικέφαλα"), getColor(load("τρικέφαλα")))
        }

        MuscleBox("κορμός", load("κορμός"), getColor(load("κορμός")))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MuscleBox("πόδια", load("πόδια"), getColor(load("πόδια")))
            MuscleBox("γλουτοί", load("γλουτοί"), getColor(load("γλουτοί")))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MuscleBox("πήχεις", load("πήχεις"), getColor(load("πήχεις")))
            MuscleBox("τραπεζοειδής", load("τραπεζοειδής"), getColor(load("τραπεζοειδής")))
        }
    }
}

// ================= MUSCLE BOX =================

@Composable
fun MuscleBox(
    name: String,
    load: Int,
    color: Color
) {

    Box(
        modifier = Modifier
            .size(width = 110.dp, height = 55.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.85f)),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text = load.toString(),
                fontSize = 11.sp,
                color = Color.Black.copy(alpha = 0.7f)
            )
        }
    }
}