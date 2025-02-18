package com.chirex.bookwatcher

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteEntry(navController: NavHostController, entries: MutableList<BookEntry>, entryIndex: Int) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Book Watcher - Delete Entry")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (entries.isEmpty() || entryIndex < 0 || entryIndex >= entries.size) {
                Text("No entries available to delete")
                Button(
                    onClick = { navController.navigate(Screens.MenuScreen.route) },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Back to Menu")
                }
            } else {
                Text("Are you sure you want to delete this entry?")
                Button(
                    onClick = {
                        entries.removeAt(entryIndex)
                        navController.navigate(Screens.MenuScreen.route)
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Delete")
                }
                Button(
                    onClick = { navController.navigate(Screens.ViewEntriesScreen.route) },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}