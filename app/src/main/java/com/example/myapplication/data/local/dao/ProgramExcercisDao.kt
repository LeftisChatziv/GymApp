package com.example.myapplication.data.local.dao

import androidx.room.*
import com.example.myapplication.data.local.entity.ProgramExerciseCrossRef

@Dao
interface ProgramExerciseDao {

    // ➕ INSERT
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRef(crossRef: ProgramExerciseCrossRef)

    // ✏️ UPDATE (CORRECT & SAFE)
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
    // 👆 επιστρέφει rows updated (χρήσιμο για debug)

    // ❌ DELETE ALL
    @Query("""
        DELETE FROM program_exercise_cross_ref 
        WHERE programId = :programId
    """)
    suspend fun deleteByProgram(programId: Int)

    // ❌ DELETE SINGLE
    @Query("""
        DELETE FROM program_exercise_cross_ref 
        WHERE programId = :programId 
        AND exerciseId = :exerciseId
    """)
    suspend fun deleteSingle(programId: Int, exerciseId: Int)

    // 📌 READ ORDERED
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
    suspend fun getByProgram(programId: Int): List<ProgramExerciseCrossRef>
}