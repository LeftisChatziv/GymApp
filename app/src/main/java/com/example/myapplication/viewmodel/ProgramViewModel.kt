package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.database.AppDatabase
import com.example.myapplication.data.local.entity.*
import com.example.myapplication.data.local.relation.ProgramWithExercises
import com.example.myapplication.data.local.relation.ProgramExerciseItem
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

    // ================= 🔥 REACTIVE FLOW =================
    val programs: StateFlow<List<ProgramWithExercises>> =
        dao.getProgramsFlow()
            .flatMapLatest { programs ->

                flow {
                    val result = programs.map { program ->
                        ProgramWithExercises(
                            program = program,
                            exercises = dao.getProgramExercises(program.id)
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

    // ================= CREATE =================
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

            exercises.forEachIndexed { index, ex ->
                dao.insertProgramExerciseCrossRef(
                    ex.copy(
                        programId = programId,
                        position = index
                    )
                )
            }
        }
    }

    // ================= SAVE =================
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
                    dao.insertProgramExerciseCrossRef(crossRef)
                } else {
                    dao.updateProgramExercise(
                        programId,
                        item.exerciseId,
                        item.sets,
                        item.reps,
                        item.weight,
                        index
                    )
                }
            }
        }
    }

    // ================= DELETE EXERCISE =================
    fun deleteExercise(programId: Int, exerciseId: Int) {
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
    fun calculateTotalVolume(exercises: List<ProgramExerciseItem>): Int {
        return exercises.sumOf {
            it.sets * it.reps * it.weight
        }
    }
}