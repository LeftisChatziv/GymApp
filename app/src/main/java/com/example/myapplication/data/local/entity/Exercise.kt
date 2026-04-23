package com.example.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.myapplication.data.local.converter.MuscleConverters

@Entity(
    tableName = "exercises",
    foreignKeys = [
        ForeignKey(
            entity = Program::class,
            parentColumns = ["id"],
            childColumns = ["programId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@TypeConverters(MuscleConverters::class)
data class Exercise(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,

    val muscleGroups: List<MuscleActivation>,

    val description: String,

    val programId: Int
)