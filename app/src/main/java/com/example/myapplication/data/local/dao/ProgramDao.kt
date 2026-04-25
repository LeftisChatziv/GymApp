package com.example.myapplication.data.local.dao

import androidx.room.*
import com.example.myapplication.data.local.entity.Program
import com.example.myapplication.data.local.entity.ProgramExerciseCrossRef
import com.example.myapplication.data.local.relation.ProgramWithExercises
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgramDao {

    // ➕ INSERT PROGRAM
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgram(program: Program): Long

    // ➕ INSERT RELATION (🔥 ΠΟΛΥ ΣΗΜΑΝΤΙΚΟ)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgramExerciseCrossRef(
        crossRef: ProgramExerciseCrossRef
    )

    // 📖 GET ALL PROGRAMS (LIVE)
    @Query("SELECT * FROM programs")
    fun getAllProgramsFlow(): Flow<List<Program>>

    // ✏️ UPDATE
    @Update
    suspend fun updateProgram(program: Program)

    // ❌ DELETE
    @Delete
    suspend fun deleteProgram(program: Program)

    // 🔗 MANY-TO-MANY RELATION
    @Transaction
    @Query("SELECT * FROM programs")
    fun getProgramsWithExercises(): Flow<List<ProgramWithExercises>>
}