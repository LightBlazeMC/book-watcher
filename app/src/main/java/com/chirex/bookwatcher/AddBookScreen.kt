import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.chirex.bookwatcher.Books
import com.chirex.bookwatcher.BooksDao
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(booksDao: BooksDao, modifier: Modifier = Modifier, navController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var added by remember { mutableStateOf<LocalDate?>(null) }
    var progress by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    val genres = listOf("Fiction", "Non Fiction", "Fantasy/Adventure/Sci-Fi", "Horror/Thriller/Crime", "Biography/History", "Poetry/Screen", "Other")
    val ratings = listOf("0", "1", "2", "3", "4", "5")
    var expandedGenre by remember { mutableStateOf(false) }
    var expandedRating by remember { mutableStateOf(false) }

    val showDatePicker = remember { mutableStateOf(false) }

    fun isValidInput(): Boolean {
        return title.isNotBlank() && author.isNotBlank() && genre.isNotBlank() && added != null && progress.isNotBlank() && rating.isNotBlank()
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
                expanded = expandedGenre,
                onExpandedChange = { expandedGenre = !expandedGenre }
            ) {
                TextField(
                    value = genre,
                    onValueChange = { genre = it },
                    label = { Text("Select Book Genre") },
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
                                genre = selectedGenre
                                expandedGenre = false
                            }
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Due Date: ${added?.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) ?: "None"}")
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { showDatePicker.value = true }) {
                    Text("Pick Due Date")
                }
            }
            if (showDatePicker.value) {
                ShowCalendar { added = it; showDatePicker.value = false }
            }

            TextField(
                value = progress,
                onValueChange = { progress = it },
                label = { Text("Enter Book Progress") },
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = expandedRating,
                onExpandedChange = { expandedRating = !expandedRating }
            ) {
                TextField(
                    value = rating,
                    onValueChange = { rating = it },
                    label = { Text("Select Book Rating") },
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
                                rating = selectedRating
                                expandedRating = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (isValidInput()) {
                        showDialog = true
                    } else {
                        snackbarMessage = "Please fill in all fields."
                        showSnackbar = true
                    }
                },
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
                                        val book = Books(title = title, author = author, genre = genre, added = added?.toString() ?: "", progress = progress, rating = rating)
                                        booksDao.insertBook(book)
                                        title = ""
                                        author = ""
                                        genre = ""
                                        added = null
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

@Composable
fun ShowCalendar(onDateSelected: (LocalDate) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}