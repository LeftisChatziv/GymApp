package com.example.myapplication.data.local.dao

import androidx.room.*
import com.example.myapplication.data.local.entity.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    // 🔥 LIVE DATA (ALL)
    @Query("SELECT * FROM exercises")
    fun getAllExercisesFlow(): Flow<List<Exercise>>

    // 📦 One-time fetch
    @Query("SELECT * FROM exercises")
    suspend fun getAllExercises(): List<Exercise>

    // ➕ INSERT single
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exercise: Exercise)

    // ➕ INSERT list
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(exercises: List<Exercise>)

    // 🔍 FILTER by programId


    // 🔥 FILTER by category (Σώμα / Βαράκια / Όργανα)
    @Query("SELECT * FROM exercises WHERE category = :category")
    fun getExercisesByCategoryFlow(category: String): Flow<List<Exercise>>

    // ✏️ UPDATE
    @Update
    suspend fun updateExercise(exercise: Exercise)

    // ❌ DELETE
    @Delete
    suspend fun delete(exercise: Exercise)
}