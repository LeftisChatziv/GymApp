package com.example.myapplication.Screens

data class RankProgress(
    val currentRank: String,
    val nextRank: String?,
    val progress: Float
)

fun calculateRankProgress(xp: Double): RankProgress {

    val thresholds = listOf(
        0.0,
        1000.0,
        2500.0,
        5000.0,
        9000.0,
        15000.0,
        25000.0
    )

    val ranks = listOf(
        "Beginner",
        "Novice",
        "Apprentice",
        "Athlete",
        "Advanced",
        "Elite",
        "Legend"
    )

    val index = thresholds.indexOfLast { xp >= it }.coerceAtLeast(0)

    val currentRank = ranks[index]
    val nextRank = if (index < thresholds.lastIndex) ranks[index + 1] else null

    if (index == thresholds.lastIndex) {
        return RankProgress(currentRank, null, 1f)
    }

    val progress =
        ((xp - thresholds[index]) /
                (thresholds[index + 1] - thresholds[index]))
            .coerceIn(0.0, 1.0)
            .toFloat()

    return RankProgress(currentRank, nextRank, progress)
}