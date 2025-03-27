package com.tecmilenio.booktrackerevidencia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecmilenio.booktrackerevidencia.ui.theme.BookTrackerEvidenciaTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookTrackerEvidenciaTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(bookViewModel: BookViewModel = viewModel(factory = BookViewModelFactory(LocalContext.current))) {
    val books by bookViewModel.allBooks.observeAsState(emptyList())
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    val filteredItems = books.filter { it.title.contains(searchText.text, ignoreCase = true) }
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(21, 37, 60, 255)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(25.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
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
                    onSearchTextChanged = { searchText = it }
                )

                Spacer(modifier = Modifier.height(40.dp))

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 150.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(1.dp),
                    verticalArrangement = Arrangement.spacedBy(50.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredItems.size) { index ->
                        BookCard(book = filteredItems[index])
                    }
                }
            }

            if (showDialog) {
                AddBookDialog(
                    onDismiss = { showDialog = false },
                    onBookAdded = { newBook ->
                        scope.launch {
                            bookViewModel.insertBook(newBook.toBookEntity())
                        }
                    }
                )
            }

            FloatingActionButton(
                onClick = { showDialog = true },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                containerColor = Color(108, 108, 108),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar libros")
            }
        }
    }
}

// Extensión para convertir entre Book y BookEntity
fun Book.toBookEntity(): BookEntity {
    return BookEntity(
        title = this.title,
        author = this.author,
        totalPages = this.totalPages,
        genre = this.genre,
        startDate = this.startDate,
        imageUri = this.imageUri
    )
}

fun BookEntity.toBook(): Book {
    return Book(
        title = this.title,
        author = this.author,
        totalPages = this.totalPages,
        genre = this.genre,
        startDate = this.startDate,
        imageUri = this.imageUri
    )
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    BookTrackerEvidenciaTheme {
        MainScreen()
    }
}

