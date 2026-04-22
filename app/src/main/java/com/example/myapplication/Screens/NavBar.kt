package com.example.myapplication.Screens

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import com.example.myapplication.R

@Composable
fun NavBar(
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    NavigationBar(
        containerColor = Color(0xFF4FC3F7)
    ) {

        NavigationBarItem(
            selected = selectedItem == "home",
            onClick = { onItemSelected("home") },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.home),
                    contentDescription = "Home"
                )
            },
            label = { Text("Αρχική") }
        )

        NavigationBarItem(
            selected = selectedItem == "exercises",
            onClick = { onItemSelected("exercises") },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.exercise),
                    contentDescription = "Exercises"
                )
            },
            label = { Text("Ασκήσεις") }
        )

        NavigationBarItem(
            selected = selectedItem == "program",
            onClick = { onItemSelected("program") },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.calendar),
                    contentDescription = "Program"
                )
            },
            label = { Text("Πρόγραμμα") }
        )

        NavigationBarItem(
            selected = selectedItem == "progress",
            onClick = { onItemSelected("progress") },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.trendingup),
                    contentDescription = "Progress"
                )
            },
            label = { Text("Πρόοδος") }
        )

        NavigationBarItem(
            selected = selectedItem == "profile",
            onClick = { onItemSelected("profile") },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.person),
                    contentDescription = "Profile"
                )
            },
            label = { Text("Προφίλ") }
        )
    }
}