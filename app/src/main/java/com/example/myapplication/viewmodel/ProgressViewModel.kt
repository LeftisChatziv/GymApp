package com.example.myapplication.viewmodel

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.database.AppDatabase
import com.example.myapplication.data.local.entity.Exercise
import com.example.myapplication.data.local.entity.WorkoutHistory
import com.example.myapplication.data.local.relation.ProgramExerciseItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.util.*

class ProgressViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).workoutHistoryDao()

    val history: StateFlow<List<WorkoutHistory>> =
        dao.getAllFlow()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    // ================= NORMALIZE MUSCLES =================
    private fun normalizeMuscle(name: String): String {
        return when (name.trim().lowercase()) {

            "chest", "στήθος" -> "στήθος"
            "back", "πλάτη" -> "πλάτη"
            "legs", "πόδια" -> "πόδια"

            "shoulders", "shoulder", "ώμοι", "ωμοι" -> "ώμοι"

            "biceps", "δικέφαλα" -> "δικέφαλα"

            "triceps", "tricep", "τρικέφαλα" -> "τρικέφαλα"

            "core", "κορμός" -> "κορμός"

            "traps", "τραπεζοειδής" -> "τραπεζοειδής"

            "glutes", "γλουτοί" -> "γλουτοί"

            "forearms", "πήχεις" -> "πήχεις"

            else -> name.trim().lowercase()
        }
    }

    // ================= MUSCLE LOAD =================
    fun calculateMuscleLoad(
        programExercises: List<ProgramExerciseItem>,
        exercises: List<Exercise>
    ): Map<String, Int> {

        if (programExercises.isEmpty() || exercises.isEmpty()) {
            return emptyMap()
        }

        val exerciseMap = exercises.associateBy { it.id }
        val result = mutableMapOf<String, Int>()

        programExercises.forEach { item ->

            val exercise = exerciseMap[item.exerciseId] ?: return@forEach

            val volume = item.sets * item.reps * item.weight

            exercise.muscleGroups.forEach { mg ->

                val key = normalizeMuscle(mg.muscle)
                val contribution = volume * (mg.percentage / 100f)

                result[key] = (result[key] ?: 0) + contribution.toInt()
            }
        }

        println("=== FINAL MUSCLE MAP ===")
        result.forEach { (k, v) ->
            println("$k -> $v")
        }

        return result
    }

    // ================= WEEKLY VOLUME =================
    fun calculateWeeklyVolume(history: List<WorkoutHistory>): List<Float> {

        val result = MutableList(7) { 0f }

        history.forEach {

            val cal = Calendar.getInstance()
            cal.timeInMillis = it.date

            val day = (cal.get(Calendar.DAY_OF_WEEK) + 5) % 7
            result[day] += it.totalVolume
        }

        return result
    }

    // ================= RECOVERY SCORE =================
    fun calculateRecoveryScore(history: List<WorkoutHistory>): Float {

        if (history.isEmpty()) return 100f

        val now = System.currentTimeMillis()

        val lastWeek = history.filter {
            now - it.date <= 7L * 24 * 60 * 60 * 1000
        }

        val totalVolume = lastWeek.sumOf { it.totalVolume.toDouble() }.toFloat()
        val sessions = lastWeek.size.coerceAtLeast(1)

        val avgVolume = totalVolume / sessions

        val intensity = (avgVolume / 20000f).coerceIn(0f, 1f)
        val frequency = (sessions / 5f).coerceIn(0f, 1f)

        return (100f - ((intensity * 60f) + (frequency * 40f)))
            .coerceIn(0f, 100f)
    }

    // ================= COLOR =================
    fun getMuscleColor(load: Int): Color {
        return when {
            load <= 0 -> Color(0xFFE0E0E0)
            load < 5000 -> Color(0xFFFF6B6B)
            load < 15000 -> Color(0xFFFFD93D)
            load < 30000 -> Color(0xFF6BCB77)
            else -> Color(0xFF4D96FF)
        }
    }
}