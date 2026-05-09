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

    // ================= CURRENT RANK =================

    fun getRank(score: Double): String {

        val index =
            thresholds.indexOfLast { score >= it }
                .coerceAtLeast(0)

        return ranks[index]
    }

    // ================= NEXT RANK =================

    fun getNextRank(score: Double): String? {

        val index =
            thresholds.indexOfLast { score >= it }

        return if (index + 1 < ranks.size)
            ranks[index + 1]
        else
            null
    }

    // ================= PROGRESS TO NEXT =================

    fun getProgress(score: Double): Float {

        val index =
            thresholds.indexOfLast { score >= it }

        if (index == thresholds.lastIndex) {
            return 1f
        }

        val currentThreshold =
            thresholds[index]

        val nextThreshold =
            thresholds[index + 1]

        val progress =
            (score - currentThreshold) /
                    (nextThreshold - currentThreshold)

        return progress.coerceIn(0.0, 1.0).toFloat()
    }

    // ================= OPTIONAL =================

    fun getRemainingXP(score: Double): Double? {

        val index =
            thresholds.indexOfLast { score >= it }

        return if (index + 1 < thresholds.size)
            thresholds[index + 1] - score
        else
            null
    }
}