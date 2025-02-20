package com.chirex.bookwatcher

import androidx.room.*

@Dao
interface BooksDao {
    @Query("SELECT * FROM books")
    suspend fun getAllBooks(): List<Books>

    @Query("SELECT * FROM books WHERE title = :title")
    suspend fun getBookByTitle(title: String): Books?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Books)

    @Query("DELETE FROM books WHERE title = :title")
    suspend fun deleteBook(title: String)
}