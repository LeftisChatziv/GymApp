package com.example.myapplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserStatsViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val uid: String
        get() = auth.currentUser?.uid ?: ""

    // ================= STATE =================
    var score by mutableStateOf(0.0)
        private set

    var streak by mutableIntStateOf(0)
        private set

    var totalWorkouts by mutableIntStateOf(0)
        private set

    var rank by mutableStateOf("Beginner")
        private set

    // ================= LISTENER =================
    fun startListening() {

        if (uid.isEmpty()) return

        firestore.collection("Users")
            .document(uid)
            .addSnapshotListener { snap, _ ->

                if (snap == null || !snap.exists()) return@addSnapshotListener

                score = snap.getDouble("score") ?: 0.0
                streak = (snap.getLong("streak") ?: 0).toInt()
                totalWorkouts = (snap.getLong("totalWorkouts") ?: 0).toInt()
                rank = snap.getString("rank") ?: "Beginner"
            }
    }

    // ================= WORKOUT UPDATE =================
    fun completeWorkout(gainedXp: Double) {

        if (uid.isEmpty()) return

        val ref = firestore.collection("Users").document(uid)

        firestore.runTransaction { tx ->

            val snap = tx.get(ref)

            val oldScore = snap.getDouble("score") ?: 0.0
            val oldStreak = (snap.getLong("streak") ?: 0).toInt()
            val oldTotal = (snap.getLong("totalWorkouts") ?: 0).toInt()
            val lastWorkout = snap.getLong("lastWorkoutDate") ?: 0L

            val now = System.currentTimeMillis()

            // ================= STREAK FIX =================
            val dayMillis = 24 * 60 * 60 * 1000L
            val lastDay = lastWorkout / dayMillis
            val currentDay = now / dayMillis

            val newStreak = when {
                lastWorkout == 0L -> 1
                currentDay == lastDay -> oldStreak
                currentDay == lastDay + 1 -> oldStreak + 1
                else -> 1
            }

            val newScore = oldScore + gainedXp
            val newRank = calculateRank(newScore)

            tx.update(
                ref,
                mapOf(
                    "score" to newScore,
                    "streak" to newStreak,
                    "totalWorkouts" to oldTotal + 1,
                    "lastWorkoutDate" to now,
                    "rank" to newRank
                )
            )
        }
    }

    private fun calculateRank(score: Double): String {
        return when {
            score >= 25000 -> "Legend"
            score >= 15000 -> "Elite"
            score >= 9000 -> "Advanced"
            score >= 5000 -> "Athlete"
            score >= 2500 -> "Apprentice"
            score >= 1000 -> "Novice"
            else -> "Beginner"
        }
    }
}