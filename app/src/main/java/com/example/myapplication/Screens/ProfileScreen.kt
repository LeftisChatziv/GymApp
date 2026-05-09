package com.example.myapplication.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R
import com.example.myapplication.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(
    onLogout: () -> Unit
) {

    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    if (user == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Please login first")
        }
        return
    }

    val userViewModel: UserViewModel = viewModel()

    // ✅ FIX: collect StateFlow σωστά
    val weight by userViewModel.weight.collectAsState(initial = 0)
    val score by userViewModel.score.collectAsState()
    val rank by userViewModel.rank.collectAsState()
    val streak by userViewModel.streak.collectAsState()
    val workouts by userViewModel.totalWorkouts.collectAsState()

    var userWeight by rememberSaveable(weight) {
        mutableIntStateOf(weight)
    }

    val email = user.email ?: ""

    // ⚠️ optional: load user once
    LaunchedEffect(Unit) {
        userViewModel.loadUser()
    }

    Scaffold { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Profile",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            // ================= USER CARD =================
            Card {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = email.firstOrNull()?.uppercaseChar()?.toString() ?: "U",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }

                    Spacer(Modifier.width(16.dp))

                    Column {
                        Text(email, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(4.dp))
                        Text("Rank: $rank")
                    }
                }
            }

            // ================= WEIGHT =================
            OutlinedTextField(
                value = userWeight.toString(),
                onValueChange = {
                    it.toIntOrNull()?.let { value ->
                        if (value > 0) {
                            userWeight = value
                        }
                    }
                },
                label = { Text("Body Weight (kg)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Button(
                onClick = {
                    userViewModel.updateWeight(userWeight)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Weight")
            }

            // ================= STATS =================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                StatCard(
                    modifier = Modifier.weight(1f),
                    title = workouts.toString(),
                    subtitle = "Workouts",
                    icon = painterResource(id = R.drawable.exercise)
                )

                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "$streak 🔥",
                    subtitle = "Streak",
                    icon = painterResource(id = R.drawable.fire)
                )

                StatCard(
                    modifier = Modifier.weight(1f),
                    title = String.format("%.1f", score),
                    subtitle = "Score",
                    icon = painterResource(id = R.drawable.trendingup)
                )
            }

            Spacer(Modifier.height(20.dp))

            // ================= DELETE PROFILE =================
            Button(
                onClick = {
                    auth.currentUser?.let {
                        auth.signOut()
                        onLogout()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            ) {
                Text("Delete Profile")
            }
        }
    }
}