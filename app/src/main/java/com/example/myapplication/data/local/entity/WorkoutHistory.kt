package com.example.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "workout_history",
    foreignKeys = [
        ForeignKey(
            entity = Program::class,
            parentColumns = ["id"],
            childColumns = ["programId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("programId")] // ✅ FIX
)
data class WorkoutHistory(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val programId: Int,

    val date: Long,

    val durationMinutes: Int
)