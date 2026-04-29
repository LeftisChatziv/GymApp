package com.example.myapplication.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.database.AppDatabase
import com.example.myapplication.data.local.entity.Exercise
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ExerciseViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).exerciseDao()

    // ================= EXERCISES FLOW =================
    val exercises: StateFlow<List<Exercise>> =
        dao.getAllExercisesFlow()
            .onStart {
                Log.d("ExerciseViewModel", "Loading exercises...")
            }
            .catch { e ->
                Log.e("ExerciseViewModel", "Error loading exercises", e)
                emit(emptyList())
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
}