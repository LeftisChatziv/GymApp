package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.compose.ui.graphics.Color
import com.example.myapplication.data.local.entity.Exercise
import com.example.myapplication.data.local.relation.ProgramExerciseItem

class ProgressViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * Υπολογίζει load ανά muscle group
     * based on volume = sets * reps * weight
     */
    fun calculateMuscleLoad(
        programExercises: List<ProgramExerciseItem>,
        exercises: List<Exercise>
    ): Map<String, Float> {

        val exerciseMap = exercises.associateBy { it.id }
        val muscleLoad = mutableMapOf<String, Float>()

        programExercises.forEach { item ->

            val exercise = exerciseMap[item.exerciseId] ?: return@forEach

            val volume = item.sets * item.reps * item.weight

            exercise.muscleGroups.forEach { activation ->

                val percentage = activation.percentage.coerceIn(0, 100)

                val load = volume * (percentage / 100f)

                muscleLoad[activation.muscle] =
                    muscleLoad.getOrDefault(activation.muscle, 0f) + load
            }
        }

        return muscleLoad
    }

    /**
     * Color scale για heatmap
     */
    fun getMuscleColor(load: Float): Color {

        return when {
            load <= 0f -> Color(0xFFE0E0E0) // empty gray
            load < 5000f -> Color(0xFFFF6B6B) // red
            load < 15000f -> Color(0xFFFFD93D) // yellow
            load < 30000f -> Color(0xFF6BCB77) // green
            else -> Color(0xFF4D96FF) // blue (overload / strong)
        }
    }
}