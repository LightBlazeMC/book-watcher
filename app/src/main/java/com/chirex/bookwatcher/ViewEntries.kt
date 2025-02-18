package com.chirex.bookwatcher

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewEntries(navController: NavHostController, entries: List<BookEntry>, onEdit: (Int) -> Unit) {
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
                entries.forEachIndexed { index, entry ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text("Title: ${entry.title}")
                        Text("Author: ${entry.author}")
                        Text("Genre: ${entry.genre}")
                        Text("Date Added: ${entry.added}")
                        Text("Progress: ${entry.progress}")
                        Text("Rating: ${entry.rating}")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(onClick = { onEdit(index) }) {
                                Text("Edit")
                            }
                            Button(onClick = { navController.navigate("deleteEntry/$index") }) {
                                Text("Delete")
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
            Button(onClick = { navController.navigate(Screens.MenuScreen.route) }) {
                Text("Back to Menu")
            }
        }
    }
}