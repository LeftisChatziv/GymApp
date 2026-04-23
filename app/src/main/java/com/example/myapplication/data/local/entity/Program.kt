package com.example.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "programs")
data class Program(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val difficulty: String
)