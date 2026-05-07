package com.example.myapplication.data.local.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.example.myapplication.data.local.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPrefsRepository(
    private val context: Context
) {

    private val weightKey = intPreferencesKey("user_weight")

    // ================= GET WEIGHT =================
    fun getUserWeight(): Flow<Int> {
        return context.dataStore.data.map { prefs ->
            prefs[weightKey] ?: 70
        }
    }

    // ================= SAVE WEIGHT =================
    suspend fun setUserWeight(weight: Int) {
        context.dataStore.edit { prefs ->
            prefs[weightKey] = weight
        }
    }
}