package com.example.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.myapplication.data.local.converter.MuscleConverters

@Entity(tableName = "exercises")
@TypeConverters(MuscleConverters::class)
data class Exercise(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,

    // 🏷️ Category (Body / Dumbbells / Machines)
    val category: String,

    // 💪 Muscle groups
    val muscleGroups: List<MuscleActivation>,

    // 📝 Description
    val description: String,

    // 🔥 Difficulty (Easy / Hard)
    val difficulty: String
)