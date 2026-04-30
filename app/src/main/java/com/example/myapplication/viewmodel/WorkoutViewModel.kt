package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.database.AppDatabase
import com.example.myapplication.data.local.entity.WorkoutHistory
import kotlinx.coroutines.launch
import java.util.Calendar

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).workoutHistoryDao()

    fun saveWorkoutHistory(
        programId: Int,
        programName: String,
        exerciseName: String,
        sets: Int,
        reps: Int,
        weight: Int,
        duration: Int,
        totalExercises: Int,
        completedExercises: Int
    ) {
        viewModelScope.launch {

            // 🔥 volume ασφαλές (αν weight = 0 δεν χαλάει τίποτα)
            val volume = sets * reps * weight + 100

            val calendar = Calendar.getInstance()

            val date = calendar.timeInMillis
            val week = calendar.get(Calendar.WEEK_OF_YEAR)
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1 // ✔️ FIX: 0-based month -> 1-12

            dao.insert(
                WorkoutHistory(
                    programId = programId,
                    programName = programName,
                    date = date,

                    // ✔️ πιο σωστό rounding
                    durationMinutes = (duration / 60f).toInt(),

                    totalExercises = totalExercises,
                    completedExercises = completedExercises,

                    totalVolume = volume.toFloat(),

                    weekNumber = week,
                    year = year,
                    month = month,

                    difficulty = "medium" // ή ό,τι θες default
                )
            )
        }
    }
}