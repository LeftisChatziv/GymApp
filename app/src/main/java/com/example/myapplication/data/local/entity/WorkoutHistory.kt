package com.example.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_history")
data class WorkoutHistory(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // 🔗 program reference
    val programId: Int,
    val programName: String,

    // ⏱️ session info
    val date: Long,                 // System.currentTimeMillis()
    val durationMinutes: Int,

    // 📊 workout stats
    val totalExercises: Int,
    val completedExercises: Int,

    // 💪 total volume (KEY για chart)
    val totalVolume: Float,

    // 📅 time grouping (IMPORTANT for analytics)
    val weekNumber: Int,
    val month: Int,   // 👈 NEW (1–12)
    val year: Int,

    // 🎯 difficulty / load category
    val difficulty: String
)