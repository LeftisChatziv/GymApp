package com.example.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_history")
data class WorkoutHistory(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val programId: Int,

    // καλύτερα να το κρατάμε σαν timestamp (milliseconds)
    val date: Long,

    val durationMinutes: Int
)