package com.tecmilenio.booktrackerevidencia

import androidx.lifecycle.LiveData

class BookRepository(private val bookDao: BookDao) {
    val allBooks: LiveData<List<Book>> = bookDao.getAll()

    suspend fun insert(book: Book) {
        bookDao.insertAll(book)
    }

    suspend fun deleteAll() {
        bookDao.deleteAll()
    }
}