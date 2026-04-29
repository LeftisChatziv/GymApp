package com.example.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_history")
data class WorkoutHistory(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // 🔗 link με program
    val programId: Int,

    val programName: String,

    val difficulty: String,

    // timestamp (System.currentTimeMillis())
    val date: Long,

    val durationMinutes: Int,

    val totalExercises: Int,

    val completedExercises: Int
)