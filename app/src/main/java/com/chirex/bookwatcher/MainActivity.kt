package com.chirex.bookwatcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chirex.bookwatcher.ui.theme.BookWatcherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookWatcherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInScreen(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf(false) }

    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    fun isValidUsername(username: String): Boolean {
        return username.isNotEmpty()
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }

    fun isValidCredentials() {
        usernameError = !isValidUsername(username)
        passwordError = !isValidPassword(password)
        if (!usernameError && !passwordError) {
            navController.navigate("MainMenu")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book Watcher - Log In") },
                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primary)
                )
        },
        content = { innerPadding ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val (logo, usernameField, passwordField, loginButton, signUpButton, forgotPassword) = createRefs()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
                    .constrainAs(logo) {
                        top.linkTo(parent.top)
                        centerHorizontallyTo(parent)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("Book Watcher", style = MaterialTheme.typography.headlineMedium)
            }

            Column(modifier = Modifier.constrainAs(usernameField) {
                top.linkTo(logo.bottom, margin = 32.dp)
            }) {
                TextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username: ") },
                    isError = usernameError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
                if (usernameError) {
                    Text(
                        text = "Username cannot be empty",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }

            Column(modifier = Modifier.constrainAs(passwordField) {
                top.linkTo(usernameField.bottom, margin = 16.dp)
            }) {
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password: ") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(
                            onClick = { passwordVisible = !passwordVisible }
                        ) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password"
                            )
                        }
                    },
                    isError = passwordError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
                if (passwordError) {
                    Text(
                        text = "Password must be at least 8 characters long.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .constrainAs(loginButton) {
                            top.linkTo(passwordField.bottom, margin = 24.dp)
                        },
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { isValidCredentials() }) {
                        Text("Log In")
                    }
                    Button(onClick = { navController.navigate("signupScreen") }) {
                        Text("Sign Up")
                    }
                }

                TextButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.constrainAs(forgotPassword) {
                        top.linkTo(loginButton.bottom, margin = 16.dp)
                        centerHorizontallyTo(parent)
                    }
                ) {
                    Text("Forgot password?")
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Book Watcher - Sign Up")
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
            Text("Sign up screen")
            Button(onClick = { navController.navigate("loginScreen") }) {
                Text("Submit")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Menu(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Book Watcher - Main Menu")
                    }
                }
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
            Button(onClick = { navController.navigate("addEntry") }) {
                Text("Add Entry")
            }
            Button(onClick = { navController.navigate("viewEntries") }) {
                Text("View Entries")
            }
            Button(onClick = { navController.navigate("editEntry") }) {
                Text("Edit Entry")
            }
            Button(onClick = { navController.navigate("deleteEntry") }) {
                Text("Delete Entry")
            }
            Button(onClick = { navController.navigate("loginScreen") }) {
                Text("Log out")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEntry(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Book Watcher - Add Entry")
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
            Text("Add Entry Screen")
            Button(onClick = { navController.navigate("MainMenu") }) {
                Text("Back to Menu")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewEntries(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Book Watcher - View Entries")
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
            Text("View Entries Screen")
            Button(onClick = { navController.navigate("MainMenu") }) {
                Text("Back to Menu")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEntry(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Book Watcher - Edit Entry")
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
            Text("Edit Entry Screen")
            Button(onClick = { navController.navigate("MainMenu") }) {
                Text("Back to Menu")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteEntry(navController: NavHostController) {
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
            Text("Delete Entry Screen")
            Button(onClick = { navController.navigate("MainMenu") }) {
                Text("Back to Menu")
            }
        }
    }
}