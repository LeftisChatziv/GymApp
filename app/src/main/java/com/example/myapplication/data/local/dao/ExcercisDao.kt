package com.example.myapplication.data.local.dao

import androidx.room.*
import com.example.myapplication.data.local.entity.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    // 🔥 LIVE DATA (Flow - για UI)
    @Query("SELECT * FROM exercises")
    fun getAllExercisesFlow(): Flow<List<Exercise>>

    // 📦 One-time fetch
    @Query("SELECT * FROM exercises")
    suspend fun getAllExercises(): List<Exercise>

    // ➕ Insert single
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exercise: Exercise)

    // ➕ Insert list
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(exercises: List<Exercise>)

    // 🔍 Filter by program
    @Query("SELECT * FROM exercises WHERE programId = :programId")
    suspend fun getExercisesByProgram(programId: Int): List<Exercise>

    // ❌ Delete
    @Delete
    suspend fun delete(exercise: Exercise)
}