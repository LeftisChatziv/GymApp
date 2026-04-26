package com.example.myapplication.data.local.dao

import androidx.room.*
import com.example.myapplication.data.local.entity.*
import com.example.myapplication.data.local.relation.ProgramExerciseItem
import com.example.myapplication.data.local.relation.ProgramWithExercises

@Dao
interface ProgramDao {

    // =====================
    // PROGRAM
    // =====================
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgram(program: Program): Long

    @Delete
    suspend fun deleteProgram(program: Program)

    // =====================
    // CROSS REF INSERT
    // =====================
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgramExerciseCrossRef(
        crossRef: ProgramExerciseCrossRef
    )

    // =====================
    // UPDATE (OPTIONAL - not needed anymore)
    // =====================
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

    // =====================
    // DELETE ALL EXERCISES OF PROGRAM
    // =====================
    @Query("""
        DELETE FROM program_exercise_cross_ref
        WHERE programId = :programId
    """)
    suspend fun deleteProgramExercises(programId: Int)

    // =====================
    // DELETE SINGLE EXERCISE
    // =====================
    @Query("""
        DELETE FROM program_exercise_cross_ref
        WHERE programId = :programId
        AND exerciseId = :exerciseId
    """)
    suspend fun deleteSingle(programId: Int, exerciseId: Int)

    // =====================
    // READ PROGRAM EXERCISES (FIXED)
    // =====================
    @Query("""
        SELECT 
            pxc.programId AS programId,
            e.id AS exerciseId,
            e.name,
            e.category,
            pxc.sets,
            pxc.reps,
            pxc.weight,
            pxc.position
        FROM program_exercise_cross_ref pxc
        INNER JOIN exercises e
            ON e.id = pxc.exerciseId
        WHERE pxc.programId = :programId
        ORDER BY pxc.position ASC
    """)
    suspend fun getProgramExercises(programId: Int): List<ProgramExerciseItem>

    // =====================
    // PROGRAM LIST
    // =====================
    @Query("SELECT * FROM programs")
    suspend fun getPrograms(): List<Program>

    @Transaction
    suspend fun getProgramsWithExercises(): List<ProgramWithExercises> {
        return getPrograms().map { program ->
            ProgramWithExercises(
                program = program,
                exercises = getProgramExercises(program.id)
            )
        }
    }
}