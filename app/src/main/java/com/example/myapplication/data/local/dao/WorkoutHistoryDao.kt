package com.example.myapplication.data.local.dao

import androidx.room.*
import com.example.myapplication.data.local.entity.WorkoutHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutHistoryDao {

    // ================= INSERT =================
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: WorkoutHistory)

    // ================= DELETE =================
    @Delete
    suspend fun delete(history: WorkoutHistory)

    // ================= 🔥 REAL-TIME =================
    @Transaction
    @Query("SELECT * FROM workout_history ORDER BY date DESC")
    fun getAllFlow(): Flow<List<WorkoutHistory>>

    // ================= WEEK =================
    @Query("""
        SELECT * FROM workout_history
        WHERE weekNumber = :weekNumber AND year = :year
        ORDER BY date ASC
    """)
    suspend fun getWeek(
        weekNumber: Int,
        year: Int
    ): List<WorkoutHistory>

    // ================= MONTH =================
    @Query("""
        SELECT * FROM workout_history
        WHERE month = :month AND year = :year
        ORDER BY date ASC
    """)
    suspend fun getMonth(
        month: Int,
        year: Int
    ): List<WorkoutHistory>

    // ================= YEAR =================
    @Query("""
        SELECT * FROM workout_history
        WHERE year = :year
        ORDER BY date ASC
    """)
    suspend fun getYear(year: Int): List<WorkoutHistory>

    // ================= LAST DAYS =================
    @Query("""
        SELECT * FROM workout_history
        WHERE date >= :fromDate
        ORDER BY date ASC
    """)
    suspend fun getFromDate(fromDate: Long): List<WorkoutHistory>

    // ================= BY PROGRAM =================
    @Query("""
        SELECT * FROM workout_history
        WHERE programId = :programId
        ORDER BY date DESC
    """)
    suspend fun getByProgram(programId: Int): List<WorkoutHistory>

    // ================= TOTAL VOLUME =================
    @Query("""
        SELECT SUM(totalVolume) FROM workout_history
        WHERE weekNumber = :weekNumber AND year = :year
    """)
    suspend fun getWeeklyTotalVolume(
        weekNumber: Int,
        year: Int
    ): Float?
}