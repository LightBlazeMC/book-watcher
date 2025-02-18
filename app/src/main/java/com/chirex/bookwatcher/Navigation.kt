package com.chirex.bookwatcher

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

//@SuppressLint("RememberReturnType")
@Composable
fun Navigation() {
    val navController = rememberNavController()
    val entries = remember { mutableStateListOf<BookEntry>() }

    NavHost(
        navController = navController,
        startDestination = "loginScreen"
    ) {
        composable("MainMenu") {
            Menu(navController)
        }
        composable("addEntry") {
            AddEntry(navController, entries)
        }
        composable("viewEntries") {
            ViewEntries(navController, entries)
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