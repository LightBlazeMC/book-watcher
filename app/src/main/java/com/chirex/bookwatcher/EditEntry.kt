package com.chirex.bookwatcher

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEntry(navController: NavHostController, entries: MutableList<BookEntry>, entryIndex: Int) {
    if (entries.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("No entries available")
            Button(onClick = { navController.navigate(Screens.MenuScreen.route) }) {
                Text("Back to Menu")
            }
        }
    }
    else {

        var title by remember { mutableStateOf(entries[entryIndex].title) }
        var author by remember { mutableStateOf(entries[entryIndex].author) }
        var genre by remember { mutableStateOf(entries[entryIndex].genre) }
        var added by remember { mutableStateOf(entries[entryIndex].added) }
        var progress by remember { mutableStateOf(entries[entryIndex].progress) }
        var rating by remember { mutableStateOf(entries[entryIndex].rating) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Book Watcher - Edit Entry")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                )
                TextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text("Author") },
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                )
                TextField(
                    value = genre,
                    onValueChange = { genre = it },
                    label = { Text("Genre") },
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                )
                TextField(
                    value = added,
                    onValueChange = { added = it },
                    label = { Text("Date Added") },
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                )
                TextField(
                    value = progress,
                    onValueChange = { progress = it },
                    label = { Text("Progress") },
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                )
                TextField(
                    value = rating,
                    onValueChange = { rating = it },
                    label = { Text("Rating") },
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                )
                Button(
                    onClick = {
                        entries[entryIndex] =
                            BookEntry(title, author, genre, added, progress, rating)
                        navController.navigate(Screens.ViewEntriesScreen.route)
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Save")
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