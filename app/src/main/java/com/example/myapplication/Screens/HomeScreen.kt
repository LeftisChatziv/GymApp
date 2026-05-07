package com.example.myapplication.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import androidx.compose.ui.res.painterResource
import com.example.myapplication.R

@Composable
fun HomeScreen(
    onLogout: () -> Unit
) {

    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    val user = auth.currentUser
    val uid = user?.uid

    var name by remember { mutableStateOf("User") }
    var rank by remember { mutableStateOf("Beginner") }

    // leaderboard stats
    var streak by remember { mutableStateOf(0) }
    var totalWorkouts by remember { mutableStateOf(0) }

    val colors = MaterialTheme.colorScheme

    // ================= LOAD DATA =================
    LaunchedEffect(uid) {

        if (uid != null) {

            try {
                // PROFILE
                val profile = firestore
                    .collection("Users")
                    .document(uid)
                    .collection("profile")
                    .document("id")
                    .get()
                    .await()

                name = profile.getString("name") ?: "User"
                rank = profile.getString("rank") ?: "Beginner"

                // LEADERBOARD
                val lb = firestore
                    .collection("leaderboard")
                    .document(uid)
                    .get()
                    .await()

                streak = (lb.getLong("streak") ?: 0L).toInt()
                totalWorkouts = (lb.getLong("totalWorkouts") ?: 0L).toInt()

            } catch (e: Exception) {
                name = user?.email ?: "User"
            }
        }
    }

    Scaffold(
        containerColor = colors.background
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {

            // ================= HEADER =================
            Text(
                text = "🏠 Home",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = colors.onBackground
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Welcome back, $name 👋",
                color = colors.onSurfaceVariant,
                fontSize = 16.sp
            )

            Text(
                text = "Rank: $rank 🏆",
                color = colors.primary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(24.dp))

            // ================= WEEKLY CARD =================
            DashboardCard(
                title = "Weekly Activity",
                subtitle = "Keep pushing your limits 💪",
                icon = painterResource(id = R.drawable.exercise)
            )

            Spacer(Modifier.height(20.dp))

            // ================= MONTHLY STATS =================
            Text(
                text = "Stats",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = colors.onBackground
            )

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                StatCard(
                    modifier = Modifier.weight(1f),
                    title = totalWorkouts.toString(),
                    subtitle = "Workouts",
                    icon = painterResource(id = R.drawable.trendingup)
                )

                StatCard(
                    modifier = Modifier.weight(1f),
                    title = streak.toString(),
                    subtitle = "Streak 🔥",
                    icon = painterResource(id = R.drawable.localfiredepartment)
                )

                StatCard(
                    modifier = Modifier.weight(1f),
                    title = rank,
                    subtitle = "Rank",
                    icon = painterResource(id = R.drawable.star)
                )
            }

            Spacer(Modifier.height(24.dp))

            // ================= NEXT WORKOUT =================
            Text(
                text = "Next Workout",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = colors.onBackground
            )

            Spacer(Modifier.height(12.dp))

            NextWorkoutCard(
                workoutTitle = "Upper Body Strength",
                day = "Thursday",
                time = "18:30"
            )

            Spacer(Modifier.height(30.dp))

            // ================= LOGOUT =================
            Button(
                onClick = {
                    auth.signOut()
                    onLogout()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = colors.onPrimary
                )
            ) {
                Text("Logout")
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}