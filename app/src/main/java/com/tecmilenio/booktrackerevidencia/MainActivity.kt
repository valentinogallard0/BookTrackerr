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
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Buscar libro...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
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
                        val book = filteredItems[index] // Usar filteredItems en lugar de books

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .size(200.dp)
                                .padding(2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(150.dp)
                                    .background(Color(141, 141, 140), shape = RoundedCornerShape(5.dp))
                                    .padding(5.dp)
                            ) {
                                if (book.imageUri != null) {
                                    Image(
                                        painter = rememberAsyncImagePainter(book.imageUri),
                                        contentDescription = "Portada del libro",
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize() // Ahora ocupa todo el espacio disponible
                                            .background(Color.Gray, shape = RoundedCornerShape(5.dp))
                                    )
                                }
                            }

                            Text(
                                text = book.title,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(top = 10.dp)
                            )
                        }
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





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookDialog(
    onDismiss: () -> Unit,
    onBookAdded: (Book) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var totalPages by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<String?>(null) }

    var errorMessage by remember { mutableStateOf<String?>(null) } // Mensaje de error

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { imageUri = it.toString() }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar Libro") },
        text = {
            Column {
                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = Color.Red,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    isError = title.isEmpty() && errorMessage != null // Resaltar campo vacío
                )
                OutlinedTextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text("Autor") },
                    isError = author.isEmpty() && errorMessage != null
                )
                OutlinedTextField(
                    value = totalPages,
                    onValueChange = { totalPages = it },
                    label = { Text("Páginas Totales") },
                    isError = (totalPages.isEmpty() || totalPages.toIntOrNull() == null) && errorMessage != null
                )
                OutlinedTextField(
                    value = genre,
                    onValueChange = { genre = it },
                    label = { Text("Género") },
                    isError = genre.isEmpty() && errorMessage != null
                )
                OutlinedTextField(
                    value = startDate,
                    onValueChange = { startDate = it },
                    label = { Text("Fecha de inicio") },
                    isError = startDate.isEmpty() && errorMessage != null
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(onClick = { launcher.launch("image/*") }) {
                    Text("Seleccionar Imagen")
                }

                imageUri?.let {
                    Image(
                        painter = rememberAsyncImagePainter(it),
                        contentDescription = "Imagen del libro",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(top = 10.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (title.isEmpty() || author.isEmpty() || totalPages.isEmpty() || genre.isEmpty() || startDate.isEmpty()) {
                    errorMessage = "Todos los campos son obligatorios"
                } else if (totalPages.toIntOrNull() == null) {
                    errorMessage = "El número de páginas debe ser un valor válido"
                } else {
                    onBookAdded(Book(title, author, totalPages.toInt(), genre, startDate, imageUri))
                    onDismiss()
                }
            }) {
                Text("Agregar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}



@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    BookTrackerEvidenciaTheme {
        MainScreen()
    }
}



