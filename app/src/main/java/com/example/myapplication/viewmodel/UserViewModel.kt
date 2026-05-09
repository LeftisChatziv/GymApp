package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val uid: String?
        get() = auth.currentUser?.uid

    // ================= STATE =================

    private val _name = MutableStateFlow("User")
    val name: StateFlow<String> = _name

    private val _rank = MutableStateFlow("Beginner")
    val rank: StateFlow<String> = _rank

    private val _score = MutableStateFlow(0.0)
    val score: StateFlow<Double> = _score

    private val _streak = MutableStateFlow(0)
    val streak: StateFlow<Int> = _streak

    private val _totalWorkouts = MutableStateFlow(0)
    val totalWorkouts: StateFlow<Int> = _totalWorkouts

    private val _weight = MutableStateFlow(70)
    val weight: StateFlow<Int> = _weight

    // ================= LOAD USER =================

    fun loadUser() {

        val id = uid ?: return

        firestore.collection("Users")
            .document(id)
            .addSnapshotListener { snapshot, _ ->

                if (snapshot == null || !snapshot.exists()) return@addSnapshotListener

                _name.value = snapshot.getString("name") ?: "User"
                _rank.value = snapshot.getString("rank") ?: "Beginner"

                _score.value = snapshot.getDouble("score") ?: 0.0
                _streak.value = (snapshot.getLong("streak") ?: 0).toInt()
                _totalWorkouts.value = (snapshot.getLong("totalWorkouts") ?: 0).toInt()

                _weight.value = (snapshot.getLong("weight") ?: 70L).toInt()
            }
    }

    // ================= UPDATE WEIGHT =================

    fun updateWeight(newWeight: Int) {

        val id = uid ?: return

        firestore.collection("Users")
            .document(id)
            .update("weight", newWeight)

        _weight.value = newWeight
    }

    // ================= WORKOUT UPDATE =================

    fun updateAfterWorkout(gainedXp: Double) {

        val id = uid ?: return
        val ref = firestore.collection("Users").document(id)

        firestore.runTransaction { tx ->

            val snap = tx.get(ref)

            val oldScore = snap.getDouble("score") ?: 0.0
            val oldStreak = (snap.getLong("streak") ?: 0).toInt()
            val oldTotal = (snap.getLong("totalWorkouts") ?: 0).toInt()
            val lastWorkout = snap.getLong("lastWorkoutDate") ?: 0L

            val now = System.currentTimeMillis()

            // ================= DAY NORMALIZATION =================
            val dayMillis = 24 * 60 * 60 * 1000L

            val lastDay = lastWorkout / dayMillis
            val currentDay = now / dayMillis

            val newStreak = when {
                lastWorkout == 0L -> 1
                currentDay == lastDay -> oldStreak // ίδια μέρα → δεν αλλάζει
                currentDay == lastDay + 1 -> oldStreak + 1 // επόμενη μέρα
                else -> 1 // χάθηκε μέρα → reset
            }

            tx.update(
                ref,
                mapOf(
                    "score" to oldScore + gainedXp,
                    "streak" to newStreak,
                    "totalWorkouts" to oldTotal + 1,
                    "lastWorkoutDate" to now
                )
            )
        }
    }

    // ================= RANK SYSTEM =================

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

    // ================= REFRESH =================

    fun refreshUser() {
        loadUser()
    }
}