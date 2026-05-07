package com.example.myapplication.data.local.dao

import androidx.room.*
import com.example.myapplication.data.local.entity.UserStatsEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface UserStatsDao {

    // ================= GET =================
    @Query("SELECT * FROM user_stats WHERE uid = :uid")
    suspend fun getStats(uid: String): UserStatsEntity?

    // ================= INSERT =================
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stats: UserStatsEntity)

    // ================= UPDATE =================
    @Update
    suspend fun update(stats: UserStatsEntity)

    // ================= UPSERT (FIXED VERSION) =================
    @Transaction
    suspend fun insertOrUpdate(stats: UserStatsEntity) {
        val existing = getStats(stats.uid)

        if (existing == null) {
            insert(stats)
        } else {
            update(stats.copy(
                // κρατάμε σωστή συγχώνευση (optional safety)
                score = stats.score,
                streak = stats.streak,
                totalWorkouts = stats.totalWorkouts,
                lastWorkoutDate = stats.lastWorkoutDate
            ))
        }
    }
    @Query("SELECT * FROM user_stats WHERE uid = :uid")
    fun getStatsFlow(uid: String): Flow<UserStatsEntity?>
    // ================= SCORE =================
    @Query("UPDATE user_stats SET score = score + :value WHERE uid = :uid")
    suspend fun addScore(uid: String, value: Double)

    // ================= WORKOUT COUNT =================
    @Query("UPDATE user_stats SET totalWorkouts = totalWorkouts + 1 WHERE uid = :uid")
    suspend fun incrementWorkouts(uid: String)

    // ================= LAST WORKOUT =================
    @Query("UPDATE user_stats SET lastWorkoutDate = :timestamp WHERE uid = :uid")
    suspend fun updateLastWorkout(uid: String, timestamp: Long)

    // ================= STREAK =================
    @Query("UPDATE user_stats SET streak = :streak WHERE uid = :uid")
    suspend fun updateStreak(uid: String, streak: Int)

    // ================= FULL WORKOUT UPDATE =================
    @Transaction
    suspend fun onWorkoutCompleted(
        uid: String,
        scoreToAdd: Double,
        newStreak: Int,
        timestamp: Long
    ) {
        addScore(uid, scoreToAdd)
        incrementWorkouts(uid)
        updateStreak(uid, newStreak)
        updateLastWorkout(uid, timestamp)
    }
}