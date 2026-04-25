package com.example.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_history")
data class WorkoutHistory(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // 🔥 αντί για programId
    val difficulty: String, // "Easy" ή "Hard"

    val date: Long,

    val durationMinutes: Int
)