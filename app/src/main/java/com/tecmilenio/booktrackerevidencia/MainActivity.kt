package com.tecmilenio.booktrackerevidencia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import coil.compose.rememberAsyncImagePainter
import com.tecmilenio.booktrackerevidencia.ui.theme.BookTrackerEvidenciaTheme

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
fun MainScreen() {
    var books by remember { mutableStateOf(listOf<Book>()) }
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    val filteredItems = books.filter { it.title.contains(searchText.text, ignoreCase = true) }

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
                        color = Color(141, 141, 140)
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
                        BookCard(book = filteredItems[index]) // Usamos BookCard aquí
                    }
                }
            }

            var showDialog by remember { mutableStateOf(false) }

            if (showDialog) {
                AddBookDialog(
                    onDismiss = { showDialog = false },
                    onBookAdded = { newBook -> books = books + newBook }
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
        MainScreen()
    }
}



