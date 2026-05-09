package com.example.myapplication.Screens

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.R

@Composable
fun NavBar(
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {

    val colors = MaterialTheme.colorScheme

    NavigationBar(

        // 🔥 AUTO THEME COLOR
        containerColor = colors.surfaceContainer,

        tonalElevation = 8.dp
    ) {

        NavigationBarItem(
            selected = selectedItem == "home",

            onClick = {
                onItemSelected("home")
            },

            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.home),
                    contentDescription = "Home"
                )
            },

            label = {
                Text("Home")
            },

            colors = NavigationBarItemDefaults.colors(

                selectedIconColor = colors.primary,
                selectedTextColor = colors.primary,

                indicatorColor = colors.primaryContainer,

                unselectedIconColor = colors.onSurfaceVariant,
                unselectedTextColor = colors.onSurfaceVariant
            )
        )

        NavigationBarItem(
            selected = selectedItem == "exercises",

            onClick = {
                onItemSelected("exercises")
            },

            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.exercise),
                    contentDescription = "Exercises"
                )
            },

            label = {
                Text("Exercises")
            },

            colors = NavigationBarItemDefaults.colors(

                selectedIconColor = colors.primary,
                selectedTextColor = colors.primary,

                indicatorColor = colors.primaryContainer,

                unselectedIconColor = colors.onSurfaceVariant,
                unselectedTextColor = colors.onSurfaceVariant
            )
        )

        NavigationBarItem(
            selected = selectedItem == "program",

            onClick = {
                onItemSelected("program")
            },

            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.calendar),
                    contentDescription = "Program"
                )
            },

            label = {
                Text("Program")
            },

            colors = NavigationBarItemDefaults.colors(

                selectedIconColor = colors.primary,
                selectedTextColor = colors.primary,

                indicatorColor = colors.primaryContainer,

                unselectedIconColor = colors.onSurfaceVariant,
                unselectedTextColor = colors.onSurfaceVariant
            )
        )

        NavigationBarItem(
            selected = selectedItem == "progress",

            onClick = {
                onItemSelected("progress")
            },

            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.trendingup),
                    contentDescription = "Progress"
                )
            },

            label = {
                Text("Progress")
            },

            colors = NavigationBarItemDefaults.colors(

                selectedIconColor = colors.primary,
                selectedTextColor = colors.primary,

                indicatorColor = colors.primaryContainer,

                unselectedIconColor = colors.onSurfaceVariant,
                unselectedTextColor = colors.onSurfaceVariant
            )
        )

        NavigationBarItem(
            selected = selectedItem == "profile",

            onClick = {
                onItemSelected("profile")
            },

            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.person),
                    contentDescription = "Profile"
                )
            },

            label = {
                Text("Profile")
            },

            colors = NavigationBarItemDefaults.colors(

                selectedIconColor = colors.primary,
                selectedTextColor = colors.primary,

                indicatorColor = colors.primaryContainer,

                unselectedIconColor = colors.onSurfaceVariant,
                unselectedTextColor = colors.onSurfaceVariant
            )
        )
    }
}