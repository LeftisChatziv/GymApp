package com.example.myapplication.data.local.relation

import androidx.room.ColumnInfo

data class ProgramExerciseItem(
    val exerciseId: Int,
    val name: String,
    val category: String,
    val sets: Int,
    val reps: Int,
    val weight: Int,
    val position: Int
)