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
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.chirex.bookwatcher.Books
import kotlinx.coroutines.launch

@Composable
fun AddBookScreen( booksDao: BooksDao, modifier: Modifier = Modifier, navController: NavHostController,) {
    val coroutineScope = rememberCoroutineScope()
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var added by remember { mutableStateOf("") }
    var progress by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }

    Column(
        modifier = modifier
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
            label = { Text("Enter Book Rating") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    if (title.isNotBlank()) {
                        val book = Books(title = title, author = author, genre = genre, added = added, progress = progress, rating = rating)
                        booksDao.insertBook(book)
                        title = ""
                        author = ""
                        genre = ""
                        added = ""
                        progress = ""
                        rating = ""
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Book")
        }
        Button(onClick = { navController.navigate("menuScreen") }) {
            Text("Return to menu")
        }
    }
}