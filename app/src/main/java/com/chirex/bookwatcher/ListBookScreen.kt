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

@Composable
fun ListBookScreen(navController: NavHostController, booksDao: BooksDao, modifier: Modifier = Modifier) {
    val books = remember { mutableStateListOf<Books>() }

    LaunchedEffect(Unit) {
        books.addAll(booksDao.getAllBooks())
    }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(books) { book ->
                BooksCard(book = book)
                HorizontalDivider()
            }
        }
        Button(onClick = { navController.navigate("menuScreen") }) {
            Text("Return to menu")
        }
    }
}

@Composable
fun BooksCard(book: Books) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = book.title, style = MaterialTheme.typography.labelSmall)
            Text(text = book.author, style = MaterialTheme.typography.labelSmall)
        }
    }
}