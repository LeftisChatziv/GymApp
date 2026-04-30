package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.ui.graphics.Color
import com.example.myapplication.data.local.database.AppDatabase
import com.example.myapplication.data.local.entity.Exercise
import com.example.myapplication.data.local.entity.WorkoutHistory
import com.example.myapplication.data.local.relation.ProgramExerciseItem
import kotlinx.coroutines.flow.*
import java.util.*

class ProgressViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).workoutHistoryDao()

    // ================= REAL DATA =================
    val history: StateFlow<List<WorkoutHistory>> =
        dao.getAllFlow()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    // ================= NORMALIZE MUSCLE KEY =================
    private fun normalizeMuscle(name: String): String {
        return name
            .trim()
            .lowercase()
            .replace("ς", "σ") // optional greek safety
    }

    // ================= MUSCLE LOAD =================
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

                val key = normalizeMuscle(activation.muscle)

                val load = volume * (activation.percentage / 100f)

                muscleLoad[key] =
                    muscleLoad.getOrDefault(key, 0f) + load
            }
        }

        return muscleLoad
    }

    // ================= WEEKLY =================
    fun calculateWeeklyVolume(history: List<WorkoutHistory>): List<Float> {

        val result = MutableList(7) { 0f }

        history.forEach {

            val cal = Calendar.getInstance()
            cal.timeInMillis = it.date

            val day = when (cal.get(Calendar.DAY_OF_WEEK)) {
                Calendar.MONDAY -> 0
                Calendar.TUESDAY -> 1
                Calendar.WEDNESDAY -> 2
                Calendar.THURSDAY -> 3
                Calendar.FRIDAY -> 4
                Calendar.SATURDAY -> 5
                Calendar.SUNDAY -> 6
                else -> 0
            }

            result[day] += it.totalVolume
        }

        return result
    }

    // ================= RECOVERY =================
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

    // ================= COLOR SCALE =================
    fun getMuscleColor(load: Float): Color {
        return when {
            load <= 0f -> Color(0xFFE0E0E0)
            load < 5000f -> Color(0xFFFF6B6B)
            load < 15000f -> Color(0xFFFFD93D)
            load < 30000f -> Color(0xFF6BCB77)
            else -> Color(0xFF4D96FF)
        }
    }
}