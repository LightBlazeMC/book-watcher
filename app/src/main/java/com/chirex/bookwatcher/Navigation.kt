import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chirex.bookwatcher.BooksDao
import com.chirex.bookwatcher.LogInScreen
import com.chirex.bookwatcher.Menu
import com.chirex.bookwatcher.SignUpScreen

@Composable
fun Navigation(booksDao: BooksDao) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "loginScreen"
    ) {
        composable("loginScreen") {
            LogInScreen(navController) {
                navController.navigate("menuScreen")
            }
        }
        composable("signUpScreen") {
            SignUpScreen(navController)
        }
        composable("menuScreen") {
            Menu(navController)
        }
        composable("listBookScreen") {
            ListBookScreen(navController = navController, booksDao = booksDao)
        }
        composable("addBookScreen") {
            AddBookScreen(navController = navController, booksDao = booksDao)
        }
    }
}