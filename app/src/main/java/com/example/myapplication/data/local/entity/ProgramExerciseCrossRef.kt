package com.example.myapplication.data.local.entity

import androidx.room.Entity

@Entity(
    tableName = "program_exercise_cross_ref",
    primaryKeys = ["programId", "exerciseId"]
)
data class ProgramExerciseCrossRef(

    val programId: Int,

    val exerciseId: Int,

    val sets: Int,

    val reps: Int,

    val weight: Int,

    val position: Int
)