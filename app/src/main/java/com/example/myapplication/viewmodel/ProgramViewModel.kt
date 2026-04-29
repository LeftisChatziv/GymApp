package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.database.AppDatabase
import com.example.myapplication.data.local.entity.*
import com.example.myapplication.data.local.relation.ProgramWithExercises
import com.example.myapplication.Screens.EditableExercise
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.myapplication.data.local.relation.ProgramExerciseItem

class ProgramViewModel(application: Application) : AndroidViewModel(application) {

    private val programDao = AppDatabase.getDatabase(application).programDao()

    // ================= PROGRAMS =================
    private val _programs = MutableStateFlow<List<ProgramWithExercises>>(emptyList())
    val programs: StateFlow<List<ProgramWithExercises>> = _programs

    // ================= SELECTED PROGRAM =================
    private val _selectedProgramId = MutableStateFlow<Int?>(null)
    val selectedProgramId: StateFlow<Int?> = _selectedProgramId

    fun selectProgram(programId: Int) {
        _selectedProgramId.value = programId
    }

    init {
        loadPrograms()
    }

    // ================= LOAD =================
    fun loadPrograms() {
        viewModelScope.launch {
            _programs.value = programDao.getProgramsWithExercises()
        }
    }

    // ================= CREATE PROGRAM =================
    fun createProgram(
        title: String,
        days: List<String>,
        exercises: List<ProgramExerciseCrossRef>
    ) {
        viewModelScope.launch {

            val programId = programDao.insertProgram(
                Program(
                    name = title,
                    days = days.joinToString(",")
                )
            ).toInt()

            // ✔ FIX: direct insert with corrected programId
            exercises.forEachIndexed { index, ex ->

                programDao.insertProgramExerciseCrossRef(
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

    // ================= SAVE ALL =================
    fun saveAll(programId: Int, list: List<EditableExercise>) {
        viewModelScope.launch {

            list.forEachIndexed { index, item ->

                val crossRef = ProgramExerciseCrossRef(
                    programId = programId,
                    exerciseId = item.exerciseId,
                    sets = item.sets,
                    reps = item.reps,
                    weight = item.weight,
                    position = index
                )

                if (item.isNew) {
                    programDao.insertProgramExerciseCrossRef(crossRef)
                } else {
                    programDao.updateProgramExercise(
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
        }
    }

    // ================= DELETE EXERCISE =================
    fun deleteExercise(programId: Int, exerciseId: Int) {
        viewModelScope.launch {
            programDao.deleteSingle(programId, exerciseId)
            loadPrograms()
        }
    }

    // ================= DELETE PROGRAM =================
    fun deleteProgram(program: Program) {
        viewModelScope.launch {
            programDao.deleteProgramExercises(program.id)
            programDao.deleteProgram(program)
            _selectedProgramId.value = null
            loadPrograms()
        }
    }
    fun calculateTotalVolume(
        exercises: List<ProgramExerciseItem>
    ): Int {
        return exercises.sumOf {
            it.sets * it.reps * it.weight
        }
    }
}