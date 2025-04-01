package com.tecmilenio.booktrackerevidencia

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class BookViewModel(private val repository: BookRepository) : ViewModel() {
    val allBooks: LiveData<List<Book>> = repository.allBooks

    fun insert(book: Book) = viewModelScope.launch {
        repository.insert(book)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}