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

    private val historyDao =
        AppDatabase.getDatabase(application).workoutHistoryDao()

    // ================= HISTORY =================

    val history: StateFlow<List<WorkoutHistory>> =
        historyDao.getAllFlow()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    // ================= MUSCLE NORMALIZER =================

    private fun normalizeMuscle(name: String): String {

        return when (name.trim().lowercase()) {

            "chest", "στήθος" ->
                "στήθος"

            "back", "πλάτη" ->
                "πλάτη"

            "legs", "πόδια" ->
                "πόδια"

            "shoulders",
            "shoulder",
            "ώμοι",
            "ωμοι" ->
                "ώμοι"

            "biceps",
            "δικέφαλα" ->
                "δικέφαλα"

            "triceps",
            "tricep",
            "τρικέφαλα" ->
                "τρικέφαλα"

            "core",
            "κορμός" ->
                "κορμός"

            "traps",
            "τραπεζοειδής" ->
                "τραπεζοειδής"

            "glutes",
            "γλουτοί" ->
                "γλουτοί"

            "forearms",
            "πήχεις" ->
                "πήχεις"

            else ->
                name.trim().lowercase()
        }
    }

    // =========================================================
    // 🔥 EFFECTIVE WEIGHT
    // =========================================================

    private fun calculateEffectiveWeight(
        exercise: Exercise,
        loggedWeight: Int,
        userWeight: Int
    ): Int {

        // ================= BODYWEIGHT EXERCISES =================

        if (loggedWeight <= 0) {

            val name = exercise.name.lowercase()

            return when {

                // Push Ups
                name.contains("push") ->
                    (userWeight * 0.64f).toInt()

                // Dips
                name.contains("dip") ->
                    (userWeight * 0.90f).toInt()

                // Pull Ups / Chin Ups
                name.contains("pull") ||
                        name.contains("chin") ->
                    userWeight

                // Squats
                name.contains("squat") ->
                    (userWeight * 0.70f).toInt()

                // Default BW
                else ->
                    userWeight
            }
        }

        // ================= NORMAL EXERCISES =================

        return loggedWeight
    }

    // =========================================================
    // 🔥 MUSCLE LOAD (FIXED + BODYWEIGHT SUPPORT)
    // =========================================================

    fun calculateMuscleLoad(
        programExercises: List<ProgramExerciseItem>,
        exercises: List<Exercise>,
        userWeight: Int
    ): Map<String, Int> {

        if (
            programExercises.isEmpty() ||
            exercises.isEmpty()
        ) {
            return emptyMap()
        }

        // Faster lookup
        val exerciseMap =
            exercises.associateBy { it.id }

        val result =
            mutableMapOf<String, Int>()

        // =====================================================
        // LOOP THROUGH ALL PROGRAM EXERCISES
        // =====================================================

        programExercises.forEach { item ->

            val exercise =
                exerciseMap[item.exerciseId]
                    ?: return@forEach

            // ================= EFFECTIVE WEIGHT =================

            val effectiveWeight =
                calculateEffectiveWeight(
                    exercise = exercise,
                    loggedWeight = item.weight,
                    userWeight = userWeight
                )

            // ================= TOTAL VOLUME =================

            val volume =
                item.sets *
                        item.reps *
                        effectiveWeight

            // ================= MUSCLE DISTRIBUTION =================

            exercise.muscleGroups.forEach { mg ->

                val key =
                    normalizeMuscle(mg.muscle)

                val contribution =
                    volume * (mg.percentage / 100f)

                result[key] =
                    (result[key] ?: 0) +
                            contribution.toInt()
            }
        }

        return result
    }

    // =========================================================
    // 🔥 WEEKLY VOLUME
    // =========================================================

    fun calculateWeeklyVolume(
        history: List<WorkoutHistory>
    ): List<Float> {

        val result =
            MutableList(7) { 0f }

        history.forEach {

            val cal =
                Calendar.getInstance()

            cal.timeInMillis = it.date

            val day =
                (cal.get(Calendar.DAY_OF_WEEK) + 5) % 7

            result[day] += it.totalVolume
        }

        return result
    }

    // =========================================================
    // 🔥 RECOVERY SCORE
    // =========================================================

    fun calculateRecoveryScore(
        history: List<WorkoutHistory>,
        score: Double
    ): Float {

        if (history.isEmpty()) return 100f

        val now = System.currentTimeMillis()

        val lastWeek = history.filter {
            now - it.date <= 7L * 24 * 60 * 60 * 1000
        }

        val totalVolume = lastWeek.sumOf {
            it.totalVolume.toDouble()
        }.toFloat()

        val sessions = lastWeek.size.coerceAtLeast(1)

        val avgVolume = totalVolume / sessions

        val intensity = (avgVolume / 20000f).coerceIn(0f, 1f)
        val frequency = (sessions / 5f).coerceIn(0f, 1f)

        // ================= RANK PROTECTION =================
        val rankLevel = when {
            score >= 25000 -> 0.35f   // Legend (πολύ ανθεκτικός)
            score >= 15000 -> 0.45f   // Elite
            score >= 9000 -> 0.55f    // Advanced
            score >= 5000 -> 0.65f    // Athlete
            score >= 2500 -> 0.75f    // Apprentice
            score >= 1000 -> 0.85f    // Novice
            else -> 1.0f              // Beginner (κανονική κόπωση)
        }

        val fatigue =
            ((intensity * 60f) + (frequency * 40f)) * rankLevel

        return (100f - fatigue).coerceIn(0f, 100f)
    }

    // =========================================================
    // 🔥 MUSCLE COLOR
    // =========================================================

    fun getMuscleColor(load: Int): Color {

        return when {

            load <= 0 ->
                Color(0xFFE0E0E0)

            load < 5000 ->
                Color(0xFFFF6B6B)

            load < 15000 ->
                Color(0xFFFFD93D)

            load < 30000 ->
                Color(0xFF6BCB77)

            else ->
                Color(0xFF4D96FF)
        }
    }
}