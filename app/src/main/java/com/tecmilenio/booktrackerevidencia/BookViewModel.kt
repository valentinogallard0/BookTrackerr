package com.tecmilenio.booktrackerevidencia

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class BookViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: BookRepository
    val allBooks: LiveData<List<BookEntity>>

    init {
        val bookDao = AppDatabase.getDatabase(application).bookDao()
        repository = BookRepository(bookDao)
        allBooks = repository.allBooks
    }

    fun insertBook(book: BookEntity) = viewModelScope.launch {
        repository.insert(book)
    }
}

class BookViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BookViewModel(application) as T
    }
}