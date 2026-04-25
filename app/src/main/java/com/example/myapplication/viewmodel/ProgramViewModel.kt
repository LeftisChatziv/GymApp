package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.database.AppDatabase
import com.example.myapplication.data.local.entity.Program
import com.example.myapplication.data.local.entity.ProgramExerciseCrossRef
import kotlinx.coroutines.launch

// 🔥 DTO από UI
data class ExercisePlan(
    val exerciseId: Int,
    val sets: Int,
    val reps: Int,
    val weight: Int
)

class ProgramViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val programDao = db.programDao()

    fun createProgram(
        title: String,
        days: List<String>,
        exercises: List<ExercisePlan>
    ) {
        viewModelScope.launch {

            // 🔥 Μετατροπή days -> String (για αποθήκευση)
            val daysString = days.joinToString(",")

            val programId = programDao.insertProgram(
                Program(
                    name = title,
                    days = daysString
                )
            ).toInt()

            // 🔥 Insert relation (Program - Exercises)
            exercises.forEach {
                programDao.insertProgramExerciseCrossRef(
                    ProgramExerciseCrossRef(
                        programId = programId,
                        exerciseId = it.exerciseId
                    )
                )
            }
        }
    }
}