package com.tecmilenio.booktrackerevidencia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecmilenio.booktrackerevidencia.ui.theme.BookTrackerEvidenciaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BookTrackerEvidenciaTheme {
                val bookRepository = remember {
                    val bookDao = AppDatabase.getDatabase(this).bookDao()
                    BookRepository(bookDao)
                }

                val viewModel: BookViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return BookViewModel(bookRepository) as T
                        }
                    }
                )

                MainScreen(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: BookViewModel) {
    val books by viewModel.allBooks.observeAsState(initial = emptyList())
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var showDialog by remember { mutableStateOf(false) }

    val filteredItems = if (searchText.text.isEmpty()) {
        books
    } else {
        books.filter { it.title.contains(searchText.text, ignoreCase = true) }
    }

    // Contenedor principal
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
                // Título
                Text(
                    text = "Book Tracker",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(255, 255, 255)
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Barra de búsqueda
                SearchBar(
                    searchText = searchText,
                    onSearchTextChanged = { searchText = it }
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Rejilla de libros
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
                    onDismiss = { showDialog = false }  // ✅ Solo necesita onDismiss
                )
            }

            // Botón flotante para agregar un nuevo libro
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

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    BookTrackerEvidenciaTheme {
        val context = LocalContext.current
        val bookDao = AppDatabase.getDatabase(context).bookDao()
        val bookRepository = BookRepository(bookDao)
        val viewModel = BookViewModel(bookRepository)

        MainScreen(viewModel)
    }
}


