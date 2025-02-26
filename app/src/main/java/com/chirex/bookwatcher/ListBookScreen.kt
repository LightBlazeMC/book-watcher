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
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.chirex.bookwatcher.Books
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListBookScreen(navController: NavHostController, booksDao: BooksDao, modifier: Modifier = Modifier) {
    val books = remember { mutableStateListOf<Books>() }
    val coroutineScope = rememberCoroutineScope()
    var bookToDelete by remember { mutableStateOf<Books?>(null) }
    var bookToEdit by remember { mutableStateOf<Books?>(null) }
    var showSnackbar by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedGenre by remember { mutableStateOf("All") }
    val genres = listOf("All", "Fiction", "Non Fiction", "Fantasy/Adventure/Sci-Fi", "Horror/Thriller/Crime", "Biography/History", "Poetry/Screen", "Other")
    var expanded by remember { mutableStateOf(false) }

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

    val filteredBooks = if (selectedGenre == "All") books else books.filter { it.genre == selectedGenre }

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
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    value = selectedGenre,
                    onValueChange = { selectedGenre = it },
                    label = { Text("Select Genre") },
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
                    genres.forEach { genre ->
                        DropdownMenuItem(
                            text = { Text(genre) },
                            onClick = {
                                selectedGenre = genre
                                expanded = false
                            }
                        )
                    }
                }
            }

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(filteredBooks) { book ->
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
            var editedAdded by remember { mutableStateOf<LocalDate?>(LocalDate.parse(bookToEdit!!.added)) }
            var editedProgress by remember { mutableStateOf(bookToEdit!!.progress) }
            var editedRating by remember { mutableStateOf(bookToEdit!!.rating) }
            val genres = listOf("Fiction", "Non Fiction", "Fantasy/Adventure/Sci-Fi", "Horror/Thriller/Crime", "Biography/History", "Poetry/Screen", "Other")
            val ratings = listOf("0", "1", "2", "3", "4", "5")
            var expandedGenre by remember { mutableStateOf(false) }
            var expandedRating by remember { mutableStateOf(false) }
            val showDatePicker = remember { mutableStateOf(false) }

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
                            expanded = expandedGenre,
                            onExpandedChange = { expandedGenre = !expandedGenre }
                        ) {
                            TextField(
                                value = editedGenre,
                                onValueChange = { editedGenre = it },
                                label = { Text("Genre") },
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGenre) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = expandedGenre,
                                onDismissRequest = { expandedGenre = false }
                            ) {
                                genres.forEach { selectedGenre ->
                                    DropdownMenuItem(
                                        text = { Text(selectedGenre) },
                                        onClick = {
                                            editedGenre = selectedGenre
                                            expandedGenre = false
                                        }
                                    )
                                }
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Added Date: ${editedAdded?.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) ?: "None"}")
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = { showDatePicker.value = true }) {
                                Text("Pick Date")
                            }
                        }
                        if (showDatePicker.value) {
                            ShowCalendar { editedAdded = it; showDatePicker.value = false }
                        }
                        TextField(
                            value = editedProgress,
                            onValueChange = { editedProgress = it },
                            label = { Text("Progress") }
                        )
                        ExposedDropdownMenuBox(
                            expanded = expandedRating,
                            onExpandedChange = { expandedRating = !expandedRating }
                        ) {
                            TextField(
                                value = editedRating,
                                onValueChange = { editedRating = it },
                                label = { Text("Rating") },
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRating) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = expandedRating,
                                onDismissRequest = { expandedRating = false }
                            ) {
                                ratings.forEach { selectedRating ->
                                    DropdownMenuItem(
                                        text = { Text(selectedRating) },
                                        onClick = {
                                            editedRating = selectedRating
                                            expandedRating = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        coroutineScope.launch {
                            val updatedBook = bookToEdit!!.copy(
                                title = editedTitle,
                                author = editedAuthor,
                                genre = editedGenre,
                                added = editedAdded.toString(),
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
            Text(text = "Title: ${book.title}", style = MaterialTheme.typography.labelSmall)
            Text(text = "Author: ${book.author}", style = MaterialTheme.typography.labelSmall)
            Text(text = "Genre: ${book.genre}", style = MaterialTheme.typography.labelSmall)
            Text(text = "Due Date: ${book.added}", style = MaterialTheme.typography.labelSmall)
            Text(text = "Progress: ${book.progress}", style = MaterialTheme.typography.labelSmall)
            Text(text = "Rating: ${book.rating}/5", style = MaterialTheme.typography.labelSmall)
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