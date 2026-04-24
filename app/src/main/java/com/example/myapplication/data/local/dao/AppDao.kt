package com.example.myapplication.data.local.dao

import androidx.room.*
import com.example.myapplication.data.local.entity.Program
import com.example.myapplication.data.local.relation.ProgramWithExercises
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgramDao {

    // ➕ INSERT
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgram(program: Program)

    // 📖 GET ALL (one-time)
    @Query("SELECT * FROM programs")
    suspend fun getAllPrograms(): List<Program>

    // 🔥 LIVE DATA (για UI)
    @Query("SELECT * FROM programs")
    fun getAllProgramsFlow(): Flow<List<Program>>

    // ✏️ UPDATE
    @Update
    suspend fun updateProgram(program: Program)

    // ❌ DELETE
    @Delete
    suspend fun deleteProgram(program: Program)

    // 🔗 RELATION (Program + Exercises)
    @Transaction
    @Query("SELECT * FROM programs")
    suspend fun getProgramsWithExercises(): List<ProgramWithExercises>
}