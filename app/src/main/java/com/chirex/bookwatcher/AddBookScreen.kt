import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavHostController
import com.chirex.bookwatcher.Books
import com.chirex.bookwatcher.BooksDao
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(booksDao: BooksDao, modifier: Modifier = Modifier, navController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var added by remember { mutableStateOf("") }
    var progress by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    val genres = listOf("Fiction", "Non Fiction", "Fantasy/Adventure/Sci-Fi", "Horror/Thriller/Crime", "Biography/History", "Poetry/Screen", "Other")
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Enter Book Title") },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = author,
                onValueChange = { author = it },
                label = { Text("Enter Book Author") },
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    value = genre,
                    onValueChange = { genre = it },
                    label = { Text("Select Book Genre") },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    genres.forEach { selectedGenre ->
                        DropdownMenuItem(
                            text = { Text(selectedGenre) },
                            onClick = {
                                genre = selectedGenre
                                expanded = false
                            }
                        )
                    }
                }
            }

            TextField(
                value = added,
                onValueChange = { added = it },
                label = { Text("Enter Book Added") },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = progress,
                onValueChange = { progress = it },
                label = { Text("Enter Book Progress") },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = rating,
                onValueChange = { rating = it },
                label = { Text("Enter Book Rating") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { showDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Book")
            }
            Button(onClick = { navController.navigate("menuScreen") }) {
                Text("Return to menu")
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Confirm Addition") },
                    text = { Text("Would you like to add the book \"$title\"?") },
                    confirmButton = {
                        Button(onClick = {
                            coroutineScope.launch {
                                if (title.isNotBlank()) {
                                    val existingBook = booksDao.getBookByTitle(title)
                                    if (existingBook != null) {
                                        snackbarMessage = "This book already exists."
                                    } else {
                                        val book = Books(title = title, author = author, genre = genre, added = added, progress = progress, rating = rating)
                                        booksDao.insertBook(book)
                                        title = ""
                                        author = ""
                                        genre = ""
                                        added = ""
                                        progress = ""
                                        rating = ""
                                        snackbarMessage = "Book added successfully."
                                    }
                                    showSnackbar = true
                                }
                            }
                            showDialog = false
                        }) {
                            Text("Yes, Add")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showDialog = false }) {
                            Text("No, Cancel")
                        }
                    }
                )
            }

            if (showSnackbar) {
                LaunchedEffect(Unit) {
                    snackbarHostState.showSnackbar(snackbarMessage)
                    delay(3000) // Dismiss after 3 seconds
                    showSnackbar = false
                }
            }
        }
    }
}