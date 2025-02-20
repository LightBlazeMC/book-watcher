package com.chirex.bookwatcher

import Navigation
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import com.chirex.bookwatcher.ui.theme.BookWatcherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = DatabaseInstance.getDatabase(applicationContext)
        val booksDao = db.booksDao()

        setContent {
            BookWatcherTheme {
                Navigation(booksDao = booksDao)
            }
        }
    }

}
