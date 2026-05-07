package com.example.myapplication.data.local.relation

import com.example.myapplication.data.local.entity.MuscleActivation

data class ProgramExerciseItem(
    val programId: Int,
    val exerciseId: Int,
    val name: String,
    val category: String,
    val sets: Int,
    val reps: Int,
    val weight: Int,
    val position: Int,

)