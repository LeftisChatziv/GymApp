package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.database.AppDatabase
import com.example.myapplication.data.local.entity.WorkoutHistory
import com.example.myapplication.data.local.relation.ProgramExerciseItem
import com.example.myapplication.data.local.repository.UserStatsRepository
import kotlinx.coroutines.launch
import java.util.Calendar
class WorkoutViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val db =
        AppDatabase.getDatabase(application)

    private val historyDao =
        db.workoutHistoryDao()

    private val statsRepository =
        UserStatsRepository(
            dao = db.userStatsDao(),
            exerciseDao = db.exerciseDao(),
            context = application
        )

    // ================= SAVE HISTORY =================
    fun saveWorkoutHistory(
        programId: Int,
        programName: String,
        durationSeconds: Int,
        totalExercises: Int,
        completedExercises: Int,
        exercises: List<ProgramExerciseItem>
    ) {

        viewModelScope.launch {

            val calendar = Calendar.getInstance()

            val totalVolume = exercises.sumOf { ex ->
                (ex.sets * ex.reps * ex.weight).toDouble()
            }.toFloat()

            val history = WorkoutHistory(
                programId = programId,
                programName = programName,
                date = calendar.timeInMillis,
                durationMinutes = durationSeconds / 60,
                totalExercises = totalExercises,
                completedExercises = completedExercises,
                totalVolume = totalVolume,
                weekNumber = calendar.get(Calendar.WEEK_OF_YEAR),
                month = calendar.get(Calendar.MONTH) + 1,
                year = calendar.get(Calendar.YEAR),
                difficulty = "medium"
            )

            historyDao.insert(history)
        }
    }

    // ================= COMPLETE SESSION =================
    fun completeWorkoutSession(
        uid: String,
        exercises: List<ProgramExerciseItem>
    ) {

        viewModelScope.launch {
            statsRepository.completeWorkout(uid, exercises)
        }
    }
}