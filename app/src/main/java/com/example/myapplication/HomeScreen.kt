package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun HomeScreen(
    onLogout: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF121212)) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🏋 Welcome to LightWeight!", color = Color.White, fontSize = 28.sp)
                Spacer(Modifier.height(30.dp))
                Button(
                    onClick = {
                        Firebase.auth.signOut()
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                ) {
                    Text("Logout", fontSize = 18.sp)
                }
            }
        }
    }
}