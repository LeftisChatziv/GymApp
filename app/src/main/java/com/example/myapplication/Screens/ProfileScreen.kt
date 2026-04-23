package com.example.myapplication.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen() {

    Scaffold { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            Text(
                text = "Profile",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card {
                Column(Modifier.padding(16.dp)) {
                    Text("User Info")
                    Spacer(Modifier.height(8.dp))
                    Text("Email: example@mail.com")
                    Text("Rank: Beginner")
                }
            }
        }
    }
}