package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.database.AppDatabase
import com.example.myapplication.data.local.entity.Program
import com.example.myapplication.data.local.entity.ProgramExerciseCrossRef
import kotlinx.coroutines.launch

data class ExercisePlan(
    val exerciseId: Int,
    val sets: Int,
    val reps: Int,
    val weight: Int
)

class ProgramViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val dao = db.programDao()

    val programs = dao.getProgramsWithExercises()

    fun createProgram(
        title: String,
        days: List<String>,
        exercises: List<ExercisePlan>
    ) {
        viewModelScope.launch {

            val daysString = days.joinToString(",")

            val programId = dao.insertProgram(
                Program(
                    name = title,
                    difficulty = "Easy", // ή dropdown μετά
                    days = daysString
                )
            ).toInt()

            exercises.forEach { plan ->
                dao.insertProgramExerciseCrossRef(
                    ProgramExerciseCrossRef(
                        programId = programId,
                        exerciseId = plan.exerciseId
                    )
                )
            }
        }
    }
}