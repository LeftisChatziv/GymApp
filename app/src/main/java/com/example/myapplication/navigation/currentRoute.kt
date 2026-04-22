package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun currentRoute(navController: NavHostController): String {
    return navController.currentBackStackEntryAsState().value?.destination?.route ?: "home"
}