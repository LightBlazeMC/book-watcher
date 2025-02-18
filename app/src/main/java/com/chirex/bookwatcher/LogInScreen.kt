package com.chirex.bookwatcher

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInScreen(navController: NavHostController, onLoginSuccess: (String) -> Unit) {
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
            onLoginSuccess(username)
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