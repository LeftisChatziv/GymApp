package com.example.myapplication.Screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun RankProgressBar(
    score: Double
) {
    val progress = RankSystem.getProgress(score)
    val currentRank = RankSystem.getRank(score)
    val nextRank = RankSystem.getNextRank(score)
    val remainingXp = RankSystem.getRemainingXP(score)

    // animated progress
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        label = "rank_progress"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {

        // ================= RANK TEXT =================
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = currentRank,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = nextRank ?: "MAX",
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(Modifier.height(8.dp))

        // ================= PROGRESS BAR =================
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp),
        )

        Spacer(Modifier.height(6.dp))

        // ================= INFO =================
        Text(
            text = if (nextRank != null)
                "Next: $nextRank • XP left: ${remainingXp?.toInt()}"
            else
                "Max rank reached 🔥",
            style = MaterialTheme.typography.bodySmall
        )
    }
}