import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.chirex.bookwatcher.BooksDao
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.chirex.bookwatcher.Books
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListBookScreen(navController: NavHostController, booksDao: BooksDao, modifier: Modifier = Modifier) {
    val books = remember { mutableStateListOf<Books>() }
    val coroutineScope = rememberCoroutineScope()
    var bookToDelete by remember { mutableStateOf<Books?>(null) }
    var bookToEdit by remember { mutableStateOf<Books?>(null) }
    var showSnackbar by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        books.addAll(booksDao.getAllBooks())
    }

    fun deleteBook(book: Books) {
        coroutineScope.launch {
            booksDao.deleteBook(book.title)
            books.remove(book)
            showSnackbar = true
        }
    }

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
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(books) { book ->
                    BooksCard(book = book, onDelete = { bookToDelete = it }, onEdit = { bookToEdit = it })
                    HorizontalDivider()
                }
            }
            Button(onClick = { navController.navigate("menuScreen") }) {
                Text("Return to menu")
            }
        }

        if (bookToDelete != null) {
            AlertDialog(
                onDismissRequest = { bookToDelete = null },
                title = { Text("Confirm Deletion") },
                text = { Text("Are you sure you want to delete the book \"${bookToDelete?.title}\"?") },
                confirmButton = {
                    Button(onClick = {
                        bookToDelete?.let { deleteBook(it) }
                        bookToDelete = null
                    }) {
                        Text("Yes, Delete")
                    }
                },
                dismissButton = {
                    Button(onClick = { bookToDelete = null }) {
                        Text("No, Cancel")
                    }
                }
            )
        }

        if (bookToEdit != null) {
            var editedTitle by remember { mutableStateOf(bookToEdit!!.title) }
            var editedAuthor by remember { mutableStateOf(bookToEdit!!.author) }
            var editedGenre by remember { mutableStateOf(bookToEdit!!.genre) }
            var editedAdded by remember { mutableStateOf(bookToEdit!!.added) }
            var editedProgress by remember { mutableStateOf(bookToEdit!!.progress) }
            var editedRating by remember { mutableStateOf(bookToEdit!!.rating) }
            val genres = listOf("Fiction", "Non Fiction", "Fantasy/Adventure/Sci-Fi", "Horror/Thriller/Crime", "Biography/History", "Poetry/Screen", "Other")
            var expanded by remember { mutableStateOf(false) }

            AlertDialog(
                onDismissRequest = { bookToEdit = null },
                title = { Text("Edit Book") },
                text = {
                    Column {
                        TextField(
                            value = editedTitle,
                            onValueChange = { editedTitle = it },
                            label = { Text("Title") }
                        )
                        TextField(
                            value = editedAuthor,
                            onValueChange = { editedAuthor = it },
                            label = { Text("Author") }
                        )
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            TextField(
                                value = editedGenre,
                                onValueChange = { editedGenre = it },
                                label = { Text("Genre") },
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
                                            editedGenre = selectedGenre
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                        TextField(
                            value = editedAdded,
                            onValueChange = { editedAdded = it },
                            label = { Text("Added") }
                        )
                        TextField(
                            value = editedProgress,
                            onValueChange = { editedProgress = it },
                            label = { Text("Progress") }
                        )
                        TextField(
                            value = editedRating,
                            onValueChange = { editedRating = it },
                            label = { Text("Rating") }
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        coroutineScope.launch {
                            val updatedBook = bookToEdit!!.copy(
                                title = editedTitle,
                                author = editedAuthor,
                                genre = editedGenre,
                                added = editedAdded,
                                progress = editedProgress,
                                rating = editedRating
                            )
                            booksDao.insertBook(updatedBook)
                            books[books.indexOfFirst { it.title == bookToEdit!!.title }] = updatedBook
                            bookToEdit = null
                        }
                    }) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    Button(onClick = { bookToEdit = null }) {
                        Text("Cancel")
                    }
                }
            )
        }

        if (showSnackbar) {
            LaunchedEffect(Unit) {
                snackbarHostState.showSnackbar("Book deleted successfully.")
                delay(3000) // Dismiss after 3 seconds
                showSnackbar = false
            }
        }
    }
}

@Composable
fun BooksCard(book: Books, onDelete: (Books) -> Unit, onEdit: ((Books) -> Unit)? = null) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = book.title, style = MaterialTheme.typography.labelSmall)
            Text(text = book.author, style = MaterialTheme.typography.labelSmall)
            Text(text = book.genre, style = MaterialTheme.typography.labelSmall)
            Text(text = book.added, style = MaterialTheme.typography.labelSmall)
            Text(text = book.progress, style = MaterialTheme.typography.labelSmall)
            Text(text = book.rating, style = MaterialTheme.typography.labelSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                onEdit?.let {
                    Button(onClick = { it(book) }) {
                        Text("Edit")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Button(onClick = { onDelete(book) }) {
                    Text("Delete")
                }
            }
        }
    }
}