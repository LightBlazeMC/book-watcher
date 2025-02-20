package com.chirex.bookwatcher
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Books (
    @PrimaryKey val title: String,
    val author: String,
    val genre: String,
    val added: String,
    val progress: String,
    val rating: String
)