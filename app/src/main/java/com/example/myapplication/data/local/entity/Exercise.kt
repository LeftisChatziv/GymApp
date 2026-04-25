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

    // 🏷️ Category
    val category: String, // Σώμα / Βαράκια / Όργανα

    // 💪 Μυϊκές ομάδες
    val muscleGroups: List<MuscleActivation>,

    // 📝 Περιγραφή
    val description: String,

    // 🔥 Difficulty αντί για programId
    val difficulty: String // "Easy" ή "Hard"
)