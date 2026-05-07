package com.example.myapplication.Screens
object WorkoutXP {

    fun calculate(
        difficulty: String,
        sets: Int,
        reps: Int,
        weight: Int
    ): Double {

        val base = when (difficulty) {
            "Easy" -> 8.0
            "Medium" -> 15.0
            "Hard" -> 30.0
            else -> 10.0
        }

        val volume = sets * reps

        val weightMultiplier = when {
            weight <= 0 -> 1.0
            weight < 20 -> 1.1
            weight < 50 -> 1.4
            else -> 1.8
        }

        return base * volume * weightMultiplier / 10.0
    }
}