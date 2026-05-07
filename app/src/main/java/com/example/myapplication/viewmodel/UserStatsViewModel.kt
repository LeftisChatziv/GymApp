package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.database.AppDatabase
import com.example.myapplication.data.local.entity.UserStatsEntity
import com.example.myapplication.data.local.relation.ProgramExerciseItem
import com.example.myapplication.data.local.repository.UserPrefsRepository
import com.example.myapplication.data.local.repository.UserStatsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserStatsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)

    // ================= REPOSITORIES =================
    private val repository = UserStatsRepository(
        dao = db.userStatsDao(),
        exerciseDao = db.exerciseDao(),
        context = application
    )

    private val prefsRepository = UserPrefsRepository(application)

    // ================= USER ID (TEMP FIX) =================
    private val uid = "demo" // 👉 ή πάρε από session/auth

    // ================= REACTIVE STATS (🔥 FIXED) =================
    val stats: StateFlow<UserStatsEntity?> =
        repository
            .getStatsFlow(uid)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )

    // ================= USER WEIGHT =================
    val userWeight: StateFlow<Int> =
        prefsRepository
            .getUserWeight()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = 70
            )

    fun setWeight(value: Int) {
        viewModelScope.launch {
            prefsRepository.setUserWeight(value)
        }
    }

    // ================= INIT USER =================
    fun initUser(uid: String) {
        viewModelScope.launch {
            repository.createIfNotExists(uid)
        }
    }

    // ================= COMPLETE WORKOUT =================
    fun completeWorkout(
        uid: String,
        exercises: List<ProgramExerciseItem>
    ) {
        viewModelScope.launch {
            repository.completeWorkout(uid, exercises)
            // ❌ ΟΧΙ loadStats πλέον
            // ❌ ΟΧΙ manual refresh
        }
    }
}