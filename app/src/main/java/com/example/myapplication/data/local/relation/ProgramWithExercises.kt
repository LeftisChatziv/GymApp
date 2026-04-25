package com.example.myapplication.data.local.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.myapplication.data.local.entity.Program
import com.example.myapplication.data.local.entity.Exercise
import com.example.myapplication.data.local.entity.ProgramExerciseCrossRef

data class ProgramWithExercises(

    @Embedded
    val program: Program,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            ProgramExerciseCrossRef::class,
            parentColumn = "programId",
            entityColumn = "exerciseId"
        )
    )
    val exercises: List<Exercise>
)