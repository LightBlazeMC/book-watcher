package com.chirex.bookwatcher

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

private val Context.dataStore by preferencesDataStore(name = "book_entries")

data class BookEntry(
    val title: String,
    val author: String,
    val genre: String,
    val added: String,
    val progress: String,
    val rating: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEntry(navController: NavHostController, context: Context) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var added by remember { mutableStateOf("") }
    var progress by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Book Watcher - Add Entry")
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
                    val newEntry = BookEntry(title, author, genre, added, progress, rating)
                    // Save entry to DataStore
                    coroutineScope.launch {
                        BookEntryDataStore.saveEntry(context, newEntry)
                        navController.navigate("MainMenu")
                    }
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Submit")
            }
            Button(
                onClick = { navController.navigate("MainMenu") },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Back to Menu")
            }
        }
    }
}