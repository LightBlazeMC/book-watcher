package com.chirex.bookwatcher

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val entries = remember { mutableStateListOf<BookEntry>() }
    var selectedEntryIndex by remember { mutableStateOf(-1) }
    var username by remember { mutableStateOf("User") }

    NavHost(
        navController = navController,
        startDestination = Screens.LogInScreenScreen.route
    ) {
        composable(Screens.MenuScreen.route) {
            Menu(navController, username)
        }
        composable(Screens.AddEntryScreen.route) {
            AddEntry(navController, LocalContext.current)
        }
        composable(Screens.ViewEntriesScreen.route) {
            ViewEntries(navController, LocalContext.current)
        }
        composable(Screens.EditEntryScreen.route) {
            EditEntry(navController, entries, selectedEntryIndex)
        }
        composable("${Screens.DeleteEntryScreen.route}/{entryIndex}") { backStackEntry ->
            val entryIndex = backStackEntry.arguments?.getString("entryIndex")?.toInt() ?: -1
            DeleteEntry(navController, entries, entryIndex)
        }
        composable(Screens.LogInScreenScreen.route) {
            LogInScreen(navController) { enteredUsername ->
                username = enteredUsername
                navController.navigate(Screens.MenuScreen.route)
            }
        }
        composable(Screens.SignUpScreenScreen.route) {
            SignUpScreen(navController)
        }
    }
}