package com.example.myapplication.data.local.entity

import androidx.room.*
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
    ],
    indices = [Index("programId")]
)
@TypeConverters(MuscleConverters::class)
data class Exercise(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,

    // 🔥 ΝΕΟ: category (Σώμα / Βαράκια / Όργανα)
    val category: String,

    val muscleGroups: List<MuscleActivation>,

    val description: String,

    val programId: Int
)