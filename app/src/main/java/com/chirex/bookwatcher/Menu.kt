package com.chirex.bookwatcher

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Menu(navController: NavHostController, username: String) {
    var entryIndex by remember { mutableStateOf(0) } // Define entryIndex variable

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Book Watcher - Main Menu")
                    }
                }
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
            Text("Hello, $username!", style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(bottom = 72.dp))
            Button(onClick = { navController.navigate("addEntry") }) {
                Text("Add Entry")
            }
            Button(onClick = { navController.navigate("viewEntries") }) {
                Text("View Entries")
            }
            Button(onClick = { navController.navigate("editEntry") }) {
                Text("Edit Entry")
            }
//            Button(onClick = { navController.navigate("deleteEntry/$entryIndex") }) { // Pass entryIndex correctly
//                Text("Delete Entry")
//            }
            Button(onClick = { navController.navigate("loginScreen") }) {
                Text("Log out")
            }
        }
    }
}