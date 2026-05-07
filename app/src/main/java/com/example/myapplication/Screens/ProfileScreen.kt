package com.example.myapplication.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.example.myapplication.viewmodel.UserStatsViewModel
import com.example.myapplication.Screens.RankSystem
import com.example.myapplication.data.local.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.example.myapplication.data.local.dataStore
import java.util.Locale

@Composable
fun ProfileScreen() {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser ?: return
    val uid = user.uid

    val viewModel: UserStatsViewModel = viewModel()
    val stats by viewModel.stats.collectAsState()

    var email by rememberSaveable { mutableStateOf(user.email ?: "") }

    // ✅ body weight state
    var userWeight by rememberSaveable { mutableIntStateOf(70) }

    // DataStore key
    val weightKey = intPreferencesKey("user_weight")

    // LOAD saved weight
    LaunchedEffect(Unit) {
        val prefs = context.dataStore.data.first()
        userWeight = prefs[weightKey] ?: 70
    }

    // Firebase load
    LaunchedEffect(uid) {
        viewModel.initUser(uid)
    }

    val streak = stats?.streak ?: 0
    val workouts = stats?.totalWorkouts ?: 0
    val score = stats?.score ?: 0.0

    val rank = RankSystem.getRank(score)
    val colors = MaterialTheme.colorScheme

    Scaffold(
        containerColor = colors.background
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // HEADER
            Text(
                text = "Profile",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            // USER CARD
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = colors.primaryContainer
                )
            ) {
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
                            .background(colors.primary),
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
                        Text("Rank: $rank")
                    }
                }
            }

            // WEIGHT INPUT
            OutlinedTextField(
                value = userWeight.toString(),
                onValueChange = {
                    it.toIntOrNull()?.let { v ->
                        if (v > 0) userWeight = v
                    }
                },
                label = { Text("Body Weight (kg)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // SAVE BUTTON
            Button(
                onClick = {
                    scope.launch {
                        context.dataStore.edit { prefs ->
                            prefs[weightKey] = userWeight
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Weight")
            }

            // STATS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                StatBox("Workouts", workouts.toString(), Modifier.weight(1f))
                StatBox("Streak", "$streak 🔥", Modifier.weight(1f))
                StatBox(
                    "Score",
                    String.format(Locale.getDefault(), "%.1f", score),
                    Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun StatBox(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}