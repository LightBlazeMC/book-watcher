import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chirex.bookwatcher.BooksDao
import com.chirex.bookwatcher.LogInScreen
import com.chirex.bookwatcher.Menu
import com.chirex.bookwatcher.SignUpScreen
import com.chirex.bookwatcher.ui.*

@Composable
fun Navigation(booksDao: BooksDao) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "loginScreen"
    ) {
        composable("loginScreen") {
            LogInScreen(navController) { enteredUsername: String ->
                navController.navigate("menuScreen")
            }
        }
        composable("signUpScreen") {
            SignUpScreen(navController)
        }
        composable("menuScreen") {
            Menu(navController, "User")
        }
        composable("listBookScreen") {
            ListBookScreen(navController = navController, booksDao = booksDao)
        }
        composable("addBookScreen") {
            AddBookScreen(navController = navController, booksDao = booksDao)
        }
    }
}