package com.example.myapplication.viewmodel
import androidx.compose.ui.graphics.Color
import com.example.myapplication.data.local.entity.Exercise
import com.example.myapplication.data.local.relation.ProgramExerciseItem
import android.app.Application
import androidx.lifecycle.AndroidViewModel

class ProgressViewModel(application: Application) : AndroidViewModel(application) {

    // ... ήδη υπάρχον code

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

                val load = volume * (activation.percentage / 100f)

                muscleLoad[activation.muscle] =
                    muscleLoad.getOrDefault(activation.muscle, 0f) + load
            }
        }

        return muscleLoad
    }

    fun getMuscleColor(load: Float): Color {
        return when {
            load < 5000 -> Color.Red
            load < 15000 -> Color.Yellow
            else -> Color.Green
        }
    }
}