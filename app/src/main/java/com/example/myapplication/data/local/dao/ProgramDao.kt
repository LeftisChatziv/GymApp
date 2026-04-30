package com.example.myapplication.data.local.dao

import androidx.room.*
import com.example.myapplication.data.local.entity.*
import com.example.myapplication.data.local.relation.ProgramExerciseItem
import com.example.myapplication.data.local.relation.ProgramWithExercises
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgramDao {

    // ================= PROGRAMS =================
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgram(program: Program): Long

    @Delete
    suspend fun deleteProgram(program: Program)

    @Query("SELECT * FROM programs")
    fun getProgramsFlow(): Flow<List<Program>>

    // ================= CROSS REF =================
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgramExerciseCrossRef(
        crossRef: ProgramExerciseCrossRef
    )

    @Query("""
        UPDATE program_exercise_cross_ref
        SET sets = :sets,
            reps = :reps,
            weight = :weight,
            position = :position
        WHERE programId = :programId
        AND exerciseId = :exerciseId
    """)
    suspend fun updateProgramExercise(
        programId: Int,
        exerciseId: Int,
        sets: Int,
        reps: Int,
        weight: Int,
        position: Int
    )

    @Query("""
        DELETE FROM program_exercise_cross_ref
        WHERE programId = :programId
    """)
    suspend fun deleteProgramExercises(programId: Int)

    @Query("""
        DELETE FROM program_exercise_cross_ref
        WHERE programId = :programId
        AND exerciseId = :exerciseId
    """)
    suspend fun deleteSingle(programId: Int, exerciseId: Int)

    // ================= EXERCISES =================
    @Query("""
        SELECT 
            pxc.programId AS programId,
            pxc.exerciseId AS exerciseId,
            e.name AS name,
            e.category AS category,
            pxc.sets AS sets,
            pxc.reps AS reps,
            pxc.weight AS weight,
            pxc.position AS position
        FROM program_exercise_cross_ref pxc
        INNER JOIN exercises e ON e.id = pxc.exerciseId
        WHERE pxc.programId = :programId
        ORDER BY pxc.position ASC
    """)
    suspend fun getProgramExercises(programId: Int): List<ProgramExerciseItem>
}