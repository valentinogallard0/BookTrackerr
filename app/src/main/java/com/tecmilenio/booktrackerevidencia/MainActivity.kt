package com.tecmilenio.booktrackerevidencia

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
import com.tecmilenio.booktrackerevidencia.ui.theme.BookTrackerEvidenciaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookTrackerEvidenciaTheme {
                // Elimina el Surface y aplica el color directamente
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var books by remember { mutableStateOf(listOf<Book>()) }
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

    // Navegación condicional
    if (selectedBook != null) {
        BookDetailScreen(
            book = selectedBook!!,
            onBack = { selectedBook = null },
            onUpdateProgress = { updatedBook ->
                books = books.map { book ->
                    if (book.title == updatedBook.title) updatedBook else book
                }
                selectedBook = updatedBook
            }
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(21, 37, 60)) // Fondo azul marino
        ) {
            Scaffold(
                containerColor = Color.Transparent, // Importante para que se vea el fondo
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
                    onBookAdded = { newBook -> books = books + newBook }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    BookTrackerEvidenciaTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(21, 37, 60))
        ) {
            MainScreen()
        }
    }
}


