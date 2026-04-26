package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.database.AppDatabase
import com.example.myapplication.data.local.entity.Program
import com.example.myapplication.data.local.entity.ProgramExerciseCrossRef
import com.example.myapplication.data.local.relation.ProgramExerciseItem
import com.example.myapplication.data.local.relation.ProgramWithExercises
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import com.example.myapplication.data.local.entity.Exercise
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.StateFlow
import com.example.myapplication.Screens.EditableExercise
data class ExercisePlan(
    val exerciseId: Int,
    val sets: Int,
    val reps: Int,
    val weight: Int,
    val position: Int
)

class ProgramViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).programDao()

    private val _programs = MutableStateFlow<List<ProgramWithExercises>>(emptyList())
    val programs: StateFlow<List<ProgramWithExercises>> = _programs

    private val _activeExercises =
        MutableStateFlow<List<ProgramExerciseItem>>(emptyList())
    val activeExercises: StateFlow<List<ProgramExerciseItem>> = _activeExercises

    init {
        loadPrograms()
    }

    // =====================
    // LOAD ALL PROGRAMS
    // =====================
    fun loadPrograms() {
        viewModelScope.launch {
            _programs.value = dao.getProgramsWithExercises()
        }
    }

    // =====================
    // LOAD EXERCISES
    // =====================
    fun loadProgramExercises(programId: Int) {
        viewModelScope.launch {
            _activeExercises.value = dao.getProgramExercises(programId)
        }
    }

    // =====================
    // CREATE PROGRAM
    // =====================
    fun createProgram(
        title: String,
        days: List<String>,
        exercises: List<ExercisePlan>
    ) {
        viewModelScope.launch {

            val programId = dao.insertProgram(
                Program(name = title, days = days.joinToString(","))
            ).toInt()

            exercises.forEachIndexed { index, ex ->
                dao.insertProgramExerciseCrossRef(
                    ProgramExerciseCrossRef(
                        programId = programId,
                        exerciseId = ex.exerciseId,
                        sets = ex.sets,
                        reps = ex.reps,
                        weight = ex.weight,
                        position = index
                    )
                )
            }

            loadPrograms()
        }
    }

    // =====================
    // UPDATE SINGLE ITEM
    // =====================
    fun updateExercise(
        programId: Int,
        item: ProgramExerciseItem,
        position: Int
    ) {
        viewModelScope.launch {

            dao.updateProgramExercise(
                programId = programId,
                exerciseId = item.exerciseId,
                sets = item.sets,
                reps = item.reps,
                weight = item.weight,
                position = position
            )

            loadProgramExercises(programId)
            loadPrograms()
        }
    }

    // =====================
    // SAVE ALL (IMPORTANT FIX)
    // =====================
    fun saveAll(programId: Int, list: List<EditableExercise>) {
        viewModelScope.launch {

            list.forEachIndexed { index, item ->

                if (item.isNew) {
                    dao.insertProgramExerciseCrossRef(
                        ProgramExerciseCrossRef(
                            programId = programId,
                            exerciseId = item.exerciseId,
                            sets = item.sets,
                            reps = item.reps,
                            weight = item.weight,
                            position = index
                        )
                    )
                } else {
                    dao.updateProgramExercise(
                        programId = programId,
                        exerciseId = item.exerciseId,
                        sets = item.sets,
                        reps = item.reps,
                        weight = item.weight,
                        position = index
                    )
                }
            }

            loadPrograms()
            loadProgramExercises(programId)
        }
    }
    // =====================
    // DELETE EXERCISE
    // =====================
    fun deleteExercise(programId: Int, exerciseId: Int) {
        viewModelScope.launch {
            dao.deleteSingle(programId, exerciseId)
            loadProgramExercises(programId)
            loadPrograms()
        }
    }

    // =====================
    // DELETE PROGRAM
    // =====================
    fun deleteProgram(program: Program) {
        viewModelScope.launch {
            dao.deleteProgramExercises(program.id)
            dao.deleteProgram(program)
            loadPrograms()
        }
    }
}