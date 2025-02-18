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
fun ViewEntries(navController: NavHostController, entries: List<BookEntry>) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Book Watcher - View Entries")
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
            if (entries.isEmpty()) {
                Text("No entries available")
            } else {
                entries.forEach { entry ->
                    Text("Title: ${entry.title}")
                    Text("Author: ${entry.author}")
                    Text("Genre: ${entry.genre}")
                    Text("Date Added: ${entry.added}")
                    Text("Progress: ${entry.progress}")
                    Text("Rating: ${entry.rating}")
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            Button(onClick = { navController.navigate("MainMenu") }) {
                Text("Back to Menu")
            }
        }
    }
}