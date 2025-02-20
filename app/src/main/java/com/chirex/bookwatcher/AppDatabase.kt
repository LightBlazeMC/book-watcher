package com.chirex.bookwatcher

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Books::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun booksDao(): BooksDao
}