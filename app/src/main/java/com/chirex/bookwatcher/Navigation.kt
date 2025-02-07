package com.chirex.bookwatcher

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "loginScreen"
    ) {
        composable("MainMenu") {
            Menu(navController)
        }
        composable("addEntry") {
            AddEntry(navController)
        }
        composable("viewEntries") {
            ViewEntries(navController)
        }
        composable("editEntry") {
            EditEntry(navController)
        }
        composable("deleteEntry") {
            DeleteEntry(navController)
        }
        composable("loginScreen") {
            LogInScreen(navController)
        }
        composable("signupScreen") {
            SignUpScreen(navController)
        }
    }
}