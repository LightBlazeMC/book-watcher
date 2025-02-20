import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.chirex.bookwatcher.Screens
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteEntry(navController: NavHostController, context: Context) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Book Watcher - Delete Entry")
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
            Text("Are you sure you want to delete this entry?")
            Button(
                onClick = {
                    coroutineScope.launch {
                        BookEntryDataStore.deleteEntry(context)
                        navController.navigate(Screens.MenuScreen.route)
                    }
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Delete")
            }
            Button(
                onClick = { navController.navigate(Screens.ViewEntriesScreen.route) },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Cancel")
            }
        }
    }
}