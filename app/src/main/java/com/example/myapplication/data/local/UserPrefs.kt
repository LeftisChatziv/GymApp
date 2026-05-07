package com.example.myapplication.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_prefs")

object UserPrefs {

    private val weightKey =
        intPreferencesKey("user_weight")

    fun getWeight(context: Context): Flow<Int> {

        return context.dataStore.data.map { prefs ->
            prefs[weightKey] ?: 70
        }
    }

    suspend fun setWeight(
        context: Context,
        weight: Int
    ) {

        context.dataStore.edit { prefs ->
            prefs[weightKey] = weight
        }
    }
}