package com.tecmilenio.booktrackerevidencia

import androidx.lifecycle.LiveData

class BookRepository(private val bookDao: BookDao) {

    val allBooks: LiveData<List<BookEntity>> = bookDao.getAllBooks()

    suspend fun insert(book: BookEntity) {
        bookDao.insertBook(book)
    }
}