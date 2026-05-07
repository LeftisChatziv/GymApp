package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.repository.UserPrefsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserPrefsViewModel(application: Application) : AndroidViewModel(application) {

    // IMPORTANT: use application context safely
    private val repo = UserPrefsRepository(application.applicationContext)

    // ================= GLOBAL WEIGHT =================
    val userWeight: StateFlow<Int> =
        repo.getUserWeight()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly, // 🔥 important fix
                initialValue = 70
            )

    // ================= UPDATE WEIGHT =================
    fun setWeight(value: Int) {
        viewModelScope.launch {
            repo.setUserWeight(value)
        }
    }
}