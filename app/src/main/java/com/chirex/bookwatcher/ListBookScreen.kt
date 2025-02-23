import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.chirex.bookwatcher.Books
import com.chirex.bookwatcher.BooksDao
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@Composable
fun ListBookScreen(navController: NavHostController, booksDao: BooksDao, modifier: Modifier = Modifier) {
    val books = remember { mutableStateListOf<Books>() }
    val coroutineScope = rememberCoroutineScope()
    var bookToDelete by remember { mutableStateOf<Books?>(null) }
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
                    BooksCard(book = book, onDelete = { bookToDelete = it })
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
fun BooksCard(book: Books, onDelete: (Books) -> Unit) {
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
            Button(onClick = { onDelete(book) }) {
                Text("Delete")
            }
        }
    }
}