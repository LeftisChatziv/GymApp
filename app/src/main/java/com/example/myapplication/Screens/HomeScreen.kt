package com.example.myapplication.Screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.myapplication.viewmodel.ProgramViewModel
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

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

    val colors = MaterialTheme.colorScheme

    // ================= ORIENTATION =================
    val configuration = LocalConfiguration.current

    val isLandscape =
        configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    // ================= PROGRAM VIEWMODEL =================
    val programViewModel: ProgramViewModel = viewModel()

    val programs by programViewModel.programs.collectAsState()

    // ================= TODAY =================
    val today = remember {
        SimpleDateFormat("EEE", Locale.ENGLISH)
            .format(Date())
            .replace(".", "")
    }

    // ================= DATE =================
    val currentDate = remember {
        SimpleDateFormat(
            "EEEE, dd MMMM yyyy",
            Locale.getDefault()
        ).format(Date())
    }

    // ================= TIME =================
    val currentTime = remember {
        SimpleDateFormat(
            "HH:mm",
            Locale.getDefault()
        ).format(Date())
    }

    // ================= TODAY PROGRAMS =================
    val todayPrograms = programs.filter {
        it.program.days.contains(today, ignoreCase = true)
    }

    // ================= LOAD USER =================
    LaunchedEffect(uid) {

        if (uid != null) {

            try {

                val document = firestore
                    .collection("Users")
                    .document(uid)
                    .get()
                    .await()

                name = document.getString("name") ?: "User"

                rank = document.getString("rank") ?: "Beginner"

            } catch (e: Exception) {

                name = user?.email ?: "User"
            }
        }
    }

    Scaffold(
        containerColor = colors.background
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ================= HEADER =================
            item {

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "🏠 Home",
                    fontSize = if (isLandscape) 34.sp else 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = colors.onBackground
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "Welcome back, $name 👋",
                    color = colors.onSurfaceVariant,
                    fontSize = if (isLandscape) 22.sp else 20.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Current Rank: $rank 🏆",
                    color = colors.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(Modifier.height(24.dp))

                // ================= DATE CARD =================
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = colors.primaryContainer
                    ),
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = 8.dp
                    )
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column {

                            Text(
                                text = "📅 Today",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = colors.onPrimaryContainer
                            )

                            Spacer(Modifier.height(8.dp))

                            Text(
                                text = currentDate,
                                color = colors.onPrimaryContainer,
                                fontSize = 15.sp
                            )

                            Spacer(Modifier.height(6.dp))

                            Text(
                                text = "⏰ $currentTime",
                                color = colors.onPrimaryContainer,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = "💪",
                            fontSize = 54.sp
                        )
                    }
                }

                Spacer(Modifier.height(28.dp))

                Text(
                    text = "Today's Programs",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = colors.onBackground
                )
            }

            // ================= EMPTY =================
            if (todayPrograms.isEmpty()) {

                item {

                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(24.dp)
                        ) {

                            Text(
                                text = "No workouts today 😴",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )

                            Spacer(Modifier.height(10.dp))

                            Text(
                                text = "Recovery is also part of progress.",
                                color = colors.onSurfaceVariant,
                                fontSize = 15.sp
                            )
                        }
                    }
                }

            } else {

                // ================= PORTRAIT =================
                if (!isLandscape) {

                    items(todayPrograms) { item ->

                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = colors.surfaceContainerHighest
                            ),
                            elevation = CardDefaults.elevatedCardElevation(
                                defaultElevation = 6.dp
                            )
                        ) {

                            Column(
                                modifier = Modifier.padding(22.dp)
                            ) {

                                Text(
                                    text = item.program.name,
                                    fontSize = 21.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.onSurface
                                )

                                Spacer(Modifier.height(10.dp))

                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = colors.primary.copy(alpha = 0.12f)
                                ) {

                                    Text(
                                        text = item.program.days,
                                        color = colors.primary,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(
                                            horizontal = 14.dp,
                                            vertical = 6.dp
                                        )
                                    )
                                }
                            }
                        }
                    }

                } else {

                    // ================= LANDSCAPE GRID =================
                    item {

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(
                                    ((todayPrograms.size + 1) / 2 * 170).dp
                                )
                        ) {

                            items(todayPrograms) { item ->

                                ElevatedCard(
                                    shape = RoundedCornerShape(24.dp),
                                    colors = CardDefaults.elevatedCardColors(
                                        containerColor =
                                            colors.surfaceContainerHighest
                                    ),
                                    elevation = CardDefaults.elevatedCardElevation(
                                        defaultElevation = 6.dp
                                    )
                                ) {

                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(22.dp),
                                        verticalArrangement =
                                            Arrangement.SpaceBetween
                                    ) {

                                        Text(
                                            text = item.program.name,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = colors.onSurface
                                        )

                                        Surface(
                                            shape = RoundedCornerShape(50),
                                            color = colors.primary.copy(alpha = 0.12f)
                                        ) {

                                            Text(
                                                text = item.program.days,
                                                color = colors.primary,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(
                                                    horizontal = 14.dp,
                                                    vertical = 6.dp
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ================= LOGOUT =================
            item {

                Spacer(Modifier.height(30.dp))

                Button(
                    onClick = {
                        auth.signOut()
                        onLogout()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary,
                        contentColor = colors.onPrimary
                    )
                ) {

                    Text(
                        text = "Logout",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(30.dp))
            }
        }
    }
}