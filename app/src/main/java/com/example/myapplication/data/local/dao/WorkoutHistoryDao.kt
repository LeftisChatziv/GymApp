package com.example.myapplication.data.local.dao

import androidx.room.*
import com.example.myapplication.data.local.entity.WorkoutHistory

@Dao
interface WorkoutHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: WorkoutHistory)

    @Query("SELECT * FROM workout_history ORDER BY date DESC")
    suspend fun getAll(): List<WorkoutHistory>

    @Delete
    suspend fun delete(history: WorkoutHistory)
}