package com.example.myapplication.data.local.dao

import androidx.room.*
import com.example.myapplication.data.local.entity.ProgramExerciseCrossRef
import com.example.myapplication.data.local.relation.ProgramExerciseItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgramExerciseDao {

    // ================= INSERT =================
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRef(crossRef: ProgramExerciseCrossRef)

    // ================= UPDATE =================
    @Query("""
        UPDATE program_exercise_cross_ref
        SET sets = :sets,
            reps = :reps,
            weight = :weight,
            position = :position
        WHERE programId = :programId
        AND exerciseId = :exerciseId
    """)
    suspend fun updateCrossRef(
        programId: Int,
        exerciseId: Int,
        sets: Int,
        reps: Int,
        weight: Int,
        position: Int
    ): Int

    // ================= DELETE ALL =================
    @Query("""
        DELETE FROM program_exercise_cross_ref 
        WHERE programId = :programId
    """)
    suspend fun deleteByProgram(programId: Int)

    // ================= DELETE SINGLE =================
    @Query("""
        DELETE FROM program_exercise_cross_ref 
        WHERE programId = :programId 
        AND exerciseId = :exerciseId
    """)
    suspend fun deleteSingle(programId: Int, exerciseId: Int)

    // =========================================================
    // 🔥 CRITICAL FIX: REACTIVE FLOW (THIS FIXES YOUR HEATMAP)
    // =========================================================
    @Query("""
        SELECT 
            programId,
            exerciseId,
            sets,
            reps,
            weight,
            position
        FROM program_exercise_cross_ref
        WHERE programId = :programId
        ORDER BY position ASC
    """)
    fun getByProgramFlow(programId: Int): Flow<List<ProgramExerciseCrossRef>>

    // (optional fallback if needed)
    @Query("""
        SELECT 
            programId,
            exerciseId,
            sets,
            reps,
            weight,
            position
        FROM program_exercise_cross_ref
        WHERE programId = :programId
        ORDER BY position ASC
    """)
    suspend fun getByProgramOnce(programId: Int): List<ProgramExerciseCrossRef>
}