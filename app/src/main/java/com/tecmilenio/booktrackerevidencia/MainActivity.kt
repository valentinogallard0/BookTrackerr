package com.tecmilenio.booktrackerevidencia

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tecmilenio.booktrackerevidencia.ui.theme.BookTrackerEvidenciaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookTrackerEvidenciaTheme {
                val bookRepository = remember { BookRepository(this) }
                var books by remember { mutableStateOf(bookRepository.loadBooks()) }

                MainScreen(
                    books = books,
                    onBooksUpdated = { updatedBooks ->
                        books = updatedBooks
                        bookRepository.saveBooks(updatedBooks)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

class BookRepository(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("BookTrackerPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val BOOKS_KEY = "SAVED_BOOKS"

    fun saveBooks(books: List<Book>) {
        val jsonBooks = gson.toJson(books)
        sharedPreferences.edit().putString(BOOKS_KEY, jsonBooks).apply()
    }

    fun loadBooks(): List<Book> {
        val jsonBooks = sharedPreferences.getString(BOOKS_KEY, null)
        return if (jsonBooks != null) {
            val type = object : TypeToken<List<Book>>() {}.type
            gson.fromJson(jsonBooks, type) ?: emptyList()
        } else {
            emptyList()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    books: List<Book>,
    onBooksUpdated: (List<Book>) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var selectedBook by remember { mutableStateOf<Book?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    val filteredBooks = if (searchText.text.isBlank()) {
        books
    } else {
        books.filter { book ->
            book.title.contains(searchText.text, ignoreCase = true) ||
                    book.author.contains(searchText.text, ignoreCase = true)
        }
    }

    if (selectedBook != null) {
        BookDetailScreen(
            book = selectedBook!!,
            onBack = { selectedBook = null },
            onUpdateProgress = { updatedBook ->
                val updatedBooks = books.map { book ->
                    if (book.title == updatedBook.title) updatedBook else book
                }
                onBooksUpdated(updatedBooks)
                selectedBook = updatedBook
            }
        )
    } else {
        Box(
            modifier = modifier.background(Color(21, 37, 60))
        ) {
            Scaffold(
                containerColor = Color.Transparent,
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { showAddDialog = true },
                        containerColor = Color(108, 108, 108),
                        contentColor = Color.White
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Agregar libro")
                    }
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .padding(25.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Book Tracker",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    SearchBar(
                        searchText = searchText,
                        onSearchTextChanged = { searchText = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 150.dp),
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(50.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(filteredBooks) { book ->
                            BookCard(
                                book = book,
                                onClick = { selectedBook = book }
                            )
                        }
                    }
                }
            }

            if (showAddDialog) {
                AddBookDialog(
                    onDismiss = { showAddDialog = false },
                    onBookAdded = { newBook ->
                        onBooksUpdated(books + newBook)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    BookTrackerEvidenciaTheme {
        MainScreen(
            books = listOf(
                Book(
                    title = "Cien años de soledad",
                    author = "Gabriel García Márquez",
                    totalPages = 432,
                    currentPages = 150,
                    genre = "Novela, Realismo mágico",
                    startDate = "2023-10-01"
                )
            ),
            onBooksUpdated = {},
            modifier = Modifier.background(Color(21, 37, 60))
        )
    }
}
