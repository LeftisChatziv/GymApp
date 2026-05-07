package com.example.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_stats")
data class UserStatsEntity(

    @PrimaryKey
    val uid: String,

    val score: Double = 0.0,

    val streak: Int = 0,

    val totalWorkouts: Int = 0,

    val lastWorkoutDate: Long = 0L
)