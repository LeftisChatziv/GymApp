package com.example.myapplication.data.local.relation

import com.example.myapplication.data.local.entity.Program

data class ProgramWithExercises(
    val program: Program,
    val exercises: List<ProgramExerciseItem> // 🔥 ΟΧΙ crossRefs
)