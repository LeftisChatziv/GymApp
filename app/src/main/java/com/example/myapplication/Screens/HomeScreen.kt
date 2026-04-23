package com.example.myapplication.Screens

import androidx.compose.foundation.layout.*
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

    val currentUser = auth.currentUser
    val userEmail = currentUser?.email ?: "User"
    val uid = currentUser?.uid

    var rank by remember { mutableStateOf("Loading...") }

    val colors = MaterialTheme.colorScheme

    LaunchedEffect(uid) {
        if (uid != null) {
            try {
                val snapshot = firestore
                    .collection("Users")
                    .document(uid)
                    .collection("profile")
                    .document("id")
                    .get()
                    .await()

                rank = snapshot.getString("rank") ?: "Beginner"

            } catch (e: Exception) {
                rank = "Error"
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
                .padding(20.dp)
        ) {

            // ================= HEADER =================
            Text(
                "🏠 Home",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = colors.onBackground
            )

            Spacer(Modifier.height(8.dp))

            Text(
                "Welcome back, $userEmail",
                color = colors.onSurfaceVariant
            )

            Text(
                "Rank: $rank 🏆",
                color = colors.primary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(24.dp))

            // ================= DASHBOARD CARD =================
            DashboardCard(
                title = "Weekly Activity",
                subtitle = "You completed 4 workouts 💪",
                icon = painterResource(id = R.drawable.exercise)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                "Monthly Stats",
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
                    title = "12",
                    subtitle = "Workouts",
                    icon = painterResource(id = R.drawable.trendingup)
                )

                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "540",
                    subtitle = "Minutes",
                    icon = painterResource(id = R.drawable.schedule)
                )

                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "3250",
                    subtitle = "Calories",
                    icon = painterResource(id = R.drawable.localfiredepartment)
                )
            }

            Spacer(Modifier.height(24.dp))

            Text(
                "Next Workout",
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

            Spacer(Modifier.weight(1f))

            Button(
                onClick = {
                    auth.signOut()
                    onLogout()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = colors.onPrimary
                )
            ) {
                Text("Logout")
            }
        }
    }
}