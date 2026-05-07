package com.example.myapplication.Screens

object RankSystem {

    private val ranks = listOf(
        "Beginner",
        "Novice",
        "Apprentice",
        "Athlete",
        "Advanced",
        "Elite",
        "Legend"
    )

    private val thresholds = listOf(
        0.0,
        1000.0,
        2500.0,
        5000.0,
        9000.0,
        15000.0,
        25000.0
    )

    fun getRank(score: Double): String {
        val index = thresholds.indexOfLast { score >= it }
            .coerceAtLeast(0)

        return ranks[index]
    }
}