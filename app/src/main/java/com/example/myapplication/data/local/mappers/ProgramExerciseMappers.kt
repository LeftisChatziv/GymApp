package com.example.myapplication.data.local.mappers

import com.example.myapplication.data.local.entity.ProgramExerciseCrossRef
import com.example.myapplication.data.local.relation.ProgramExerciseItem
import com.example.myapplication.data.local.entity.Exercise

fun ProgramExerciseCrossRef.toItem(exercise: Exercise): ProgramExerciseItem {
    return ProgramExerciseItem(
        programId = programId,
        exerciseId = exerciseId,
        name = exercise.name,
        category = exercise.category,
        sets = sets,
        reps = reps,
        weight = weight,
        position = position
    )
}