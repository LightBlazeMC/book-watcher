package com.chirex.bookwatcher
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chirex.bookwatcher.ui.theme.BookWatcherTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the Room database instance
        val db = DatabaseInstance.getDatabase(applicationContext)
        val booksDao = db.booksDao()

        setContent {
            BookWatcherTheme {
                // Scaffold UI with padding
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    BookListScreen(
                        booksDao = booksDao,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun BookListScreen(booksDao: BooksDao, modifier: Modifier = Modifier) {
    // Coroutine scope for database operations
    val coroutineScope = rememberCoroutineScope()

    // Mutable state list to store fruits from the database
    val books = remember { mutableStateListOf<Books>() }

    // State to handle new fruit input
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var added by remember { mutableStateOf("") }
    var progress by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }

    // Load fruits from the database when the composable is first displayed
    LaunchedEffect(Unit) {
        books.addAll(booksDao.getAllBooks())
    }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        // Input for adding new fruits
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

        TextField(
            value = genre,
            onValueChange = { genre = it },
            label = { Text("Enter Book Genre") },
            modifier = Modifier.fillMaxWidth()
        )

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
            label = { Text("Enter Book rating") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Button to add the new fruit
        Button(
            onClick = {
                coroutineScope.launch {
                    // Add a fruit to the database and refresh the list
                    if (title.isNotBlank()) {
                        val book = Books(title = title, author = author, genre = genre, added = added, progress = progress, rating = rating) // Default emoji for simplicity
                        booksDao.insertBook(book)
                        books.clear()
                        books.addAll(booksDao.getAllBooks())
                        title = "" // Clear input
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Book")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // LazyColumn to display the list of fruits
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(books) { book ->
                BooksCard(book = book)
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun BooksCard(book: Books) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "${book.title} by ${book.author}")
        }
    }
}


