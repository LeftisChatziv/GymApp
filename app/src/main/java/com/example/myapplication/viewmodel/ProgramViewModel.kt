package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.database.AppDatabase
import com.example.myapplication.data.local.entity.Program
import com.example.myapplication.data.local.entity.ProgramExerciseCrossRef
import com.example.myapplication.data.local.relation.ProgramExerciseItem
import com.example.myapplication.data.local.relation.ProgramWithExercises
import com.example.myapplication.Screens.EditableExercise
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProgramViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).programDao()

    // ================= SELECTED =================
    private val _selectedProgramId = MutableStateFlow<Int?>(null)
    val selectedProgramId = _selectedProgramId.asStateFlow()

    fun selectProgram(id: Int) {
        _selectedProgramId.value = id
    }

    // ================= PROGRAMS FLOW (STABLE FIX) =================
    val programs: StateFlow<List<ProgramWithExercises>> =
        dao.getProgramsFlow()
            .flatMapLatest { programs ->

                flow {

                    val result = programs.map { program ->

                        val exercises =
                            dao.getProgramExercises(program.id) // synced per item but stable emission

                        ProgramWithExercises(
                            program = program,
                            exercises = exercises
                        )
                    }

                    emit(result)
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    // ================= CREATE PROGRAM =================
    fun createProgram(
        title: String,
        days: List<String>,
        exercises: List<ProgramExerciseCrossRef>
    ) {

        viewModelScope.launch {

            val programId = dao.insertProgram(
                Program(
                    name = title,
                    days = days.joinToString(",")
                )
            ).toInt()

            exercises.forEachIndexed { index, exercise ->

                dao.insertProgramExerciseCrossRef(
                    exercise.copy(
                        programId = programId,
                        position = index
                    )
                )
            }
        }
    }

    // ================= SAVE ALL =================
    fun saveAll(
        programId: Int,
        list: List<EditableExercise>
    ) {

        viewModelScope.launch {

            dao.deleteProgramExercises(programId)

            list.forEachIndexed { index, item ->

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
            }
        }
    }

    // ================= DELETE SINGLE EXERCISE =================
    fun deleteExercise(
        programId: Int,
        exerciseId: Int
    ) {

        viewModelScope.launch {
            dao.deleteSingle(programId, exerciseId)
        }
    }

    // ================= DELETE PROGRAM =================
    fun deleteProgram(program: Program) {

        viewModelScope.launch {

            dao.deleteProgramExercises(program.id)
            dao.deleteProgram(program)

            _selectedProgramId.value = null
        }
    }

    // ================= TOTAL VOLUME =================
    fun calculateTotalVolume(
        exercises: List<ProgramExerciseItem>
    ): Int {

        return exercises.sumOf {
            it.sets * it.reps * it.weight
        }
    }
}