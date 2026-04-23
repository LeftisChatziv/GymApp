package com.example.myapplication.data.local.dao

import androidx.room.*
import com.example.myapplication.data.local.entity.Program
import com.example.myapplication.data.local.relation.ProgramWithExercises
import com.example.myapplication.data.local.entity.Exercise
@Dao
interface ProgramDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgram(program: Program)

    @Query("SELECT * FROM programs")
    suspend fun getAllPrograms(): List<Program>

    @Transaction
    @Query("SELECT * FROM programs")
    suspend fun getProgramsWithExercises(): List<ProgramWithExercises>
}
