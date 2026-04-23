package com.example.myapplication.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.myapplication.data.local.entity.Program
import com.example.myapplication.data.local.entity.Exercise

data class ProgramWithExercises(

    @Embedded
    val program: Program,

    @Relation(
        parentColumn = "id",
        entityColumn = "programId"
    )
    val exercises: List<Exercise>
)