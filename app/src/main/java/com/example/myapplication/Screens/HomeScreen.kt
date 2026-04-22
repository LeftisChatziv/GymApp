package com.example.myapplication.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.myapplication.R

@Composable
fun HomeScreen(
    onLogout: () -> Unit
) {

    val currentUser = remember { Firebase.auth.currentUser }
    val userEmail = currentUser?.email ?: "User"

    var rank by remember { mutableStateOf("Loading...") }

    val uid = currentUser?.uid

    LaunchedEffect(uid) {
        uid?.let {
            Firebase.firestore.collection("Users")
                .document(it)
                .collection("profile")
                .document("id")
                .get()
                .addOnSuccessListener { document ->
                    rank = document.getString("rank") ?: "Beginner"
                }
                .addOnFailureListener {
                    rank = "Error"
                }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
        ) {

            Text(
                "🏠 Home",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Welcome back, $userEmail",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )

            Text(
                "Rank: $rank 🏆",
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            DashboardCard(
                title = "Weekly Activity",
                subtitle = "You completed 4 workouts 💪",
                icon = painterResource(id = R.drawable.exercise)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Monthly Stats",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 22.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

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

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Next Workout",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 22.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            NextWorkoutCard(
                workoutTitle = "Upper Body Strength",
                day = "Thursday",
                time = "18:30"
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    Firebase.auth.signOut()
                    onLogout()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Logout")
            }
        }
    }
}