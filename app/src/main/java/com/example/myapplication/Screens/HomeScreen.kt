package com.example.myapplication.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.myapplication.R
import com.google.firebase.firestore.ktx.firestore
import androidx.compose.runtime.*

@Composable
fun HomeScreen(onLogout: () -> Unit) {
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
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF0F1115)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {

            // Header
            Text(
                text = "🏠 Home",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Welcome back, $userEmail",
                color = Color.LightGray,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Rank: $rank 🏆",
                color = Color(0xFF66BB6A),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Weekly Activity Card
            DashboardCard(
                title = "Weekly Activity",
                subtitle = "You completed 4 workouts this week 💪",
                icon = painterResource(id = R.drawable.exercise) // εικόνα από drawable
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Monthly Stats Row
            Text(
                text = "Monthly Stats",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold
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
                    title = "3,250",
                    subtitle = "Calories",
                    icon = painterResource(id = R.drawable.localfiredepartment)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Next Workout
            Text(
                text = "Next Workout",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            NextWorkoutCard(
                workoutTitle = "Upper Body Strength",
                day = "Thursday",
                time = "18:30"
            )

            Spacer(modifier = Modifier.weight(1f))

            // Logout Button
            Button(
                onClick = {
                    Firebase.auth.signOut()
                    onLogout()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
            ) {
                Text(
                    text = "Logout",
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun DashboardCard(
    title: String,
    subtitle: String,
    icon: Painter
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1F27)),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .background(
                        color = Color(0xFF263238),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    tint = Color(0xFF66BB6A),
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = subtitle,
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: Painter
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1F27)),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = Color(0xFF66BB6A),
                modifier = Modifier.size(26.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                color = Color.LightGray,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun NextWorkoutCard(
    workoutTitle: String,
    day: String,
    time: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            Color(0xFF2E7D32),
                            Color(0xFF66BB6A)
                        )
                    ),
                    shape = RoundedCornerShape(22.dp)
                )
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = workoutTitle,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.calendar),
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = day,
                        color = Color.White,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.schedule),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = time,
                        color = Color.White,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}