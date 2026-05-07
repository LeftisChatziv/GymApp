package com.example.myapplication.data.local.repository

import android.content.Context
import com.example.myapplication.data.local.UserPrefs
import com.example.myapplication.Screens.WorkoutXP
import com.example.myapplication.data.local.dao.ExerciseDao
import com.example.myapplication.data.local.dao.UserStatsDao
import com.example.myapplication.data.local.entity.UserStatsEntity
import com.example.myapplication.data.local.relation.ProgramExerciseItem
import kotlinx.coroutines.flow.first

class UserStatsRepository(
    private val dao: UserStatsDao,
    private val exerciseDao: ExerciseDao,
    private val context: Context
) {

    // ================= GET (ONE SHOT - only for init) =================
    suspend fun getStats(uid: String): UserStatsEntity? {
        return dao.getStats(uid)
    }

    // ================= FLOW (🔥 NEW - REQUIRED FOR UI) =================
    fun getStatsFlow(uid: String) =
        dao.getStatsFlow(uid)

    // ================= CREATE USER =================
    suspend fun createIfNotExists(uid: String) {

        val existing = dao.getStats(uid)

        if (existing == null) {
            dao.insert(
                UserStatsEntity(
                    uid = uid,
                    score = 0.0,
                    streak = 0,
                    totalWorkouts = 0,
                    lastWorkoutDate = 0L
                )
            )
        }
    }

    // ================= USER WEIGHT =================
    suspend fun getUserWeight(): Int {
        return UserPrefs.getWeight(context).first()
    }

    suspend fun setUserWeight(weight: Int) {
        UserPrefs.setWeight(context, weight)
    }

    // ================= COMPLETE WORKOUT =================
    suspend fun completeWorkout(
        uid: String,
        exercises: List<ProgramExerciseItem>
    ) {

        val stats = dao.getStats(uid) ?: return

        val now = System.currentTimeMillis()

        val allExercises =
            exerciseDao.getAllExercises()
                .associateBy { it.id }

        // ================= XP CALC =================
        val gainedXp = exercises.sumOf { ex ->

            val exercise = allExercises[ex.exerciseId]

            val difficulty = exercise?.difficulty ?: "Easy"

            WorkoutXP.calculate(
                difficulty = difficulty,
                sets = ex.sets,
                reps = ex.reps,
                weight = ex.weight
            )
        }

        // ================= STREAK LOGIC =================
        val oneDay = 24 * 60 * 60 * 1000L

        val newStreak =
            if (stats.lastWorkoutDate > 0 &&
                now - stats.lastWorkoutDate <= oneDay * 2
            ) {
                stats.streak + 1
            } else {
                1
            }

        // ================= UPDATE =================
        dao.update(
            stats.copy(
                score = stats.score + gainedXp,
                streak = newStreak,
                totalWorkouts = stats.totalWorkouts + 1,
                lastWorkoutDate = now
            )
        )
    }

    // ================= ADD SCORE (OPTIONAL) =================
    suspend fun addScore(uid: String, value: Double) {

        val stats = dao.getStats(uid) ?: return

        dao.update(
            stats.copy(
                score = stats.score + value
            )
        )
    }

    // ================= INCREMENT WORKOUT =================
    suspend fun incrementWorkout(uid: String) {

        val stats = dao.getStats(uid) ?: return

        dao.update(
            stats.copy(
                totalWorkouts = stats.totalWorkouts + 1
            )
        )
    }
}