package com.tecmilenio.booktrackerevidencia

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BookDetailScreen(
    book: Book,
    onBack: () -> Unit,
    onUpdateProgress: (Book) -> Unit
) {
    var currentPages by remember { mutableStateOf(book.currentPages) }
    var showEditDialog by remember { mutableStateOf(false) }

    // Calculamos el progreso
    val progress = if (book.totalPages > 0) {
        (currentPages.toFloat() / book.totalPages.toFloat()).coerceIn(0f, 1f)
    } else 0f

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = book.title,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Atrás",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(21, 37, 60),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color(21, 37, 60))
        ) {
            // Portada del libro
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                book.imageUri?.let { uri ->
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = "Portada del libro",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } ?: run {
                    Text(
                        "Sin portada",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                }
            }

            // Información del libro
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = book.author,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Géneros
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    book.getGenresList().forEach { genre ->
                        FilterChip(
                            selected = true,
                            onClick = {},
                            label = {
                                Text(
                                    text = genre,
                                    color = Color.Black
                                )
                            },
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }

                // Progreso de lectura
                Column(
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    Text(
                        text = "Progreso:",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = progress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        color = Color(0xFF4CAF50),
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "$currentPages/${book.totalPages} páginas",
                        color = Color.White
                    )
                    Button(
                        onClick = { showEditDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Text("Actualizar progreso")
                    }
                }

                // Fecha de inicio
                Text(
                    text = "Iniciado: ${book.startDate}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray
                )
            }
        }
    }

    // Diálogo para actualizar progreso
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Actualizar progreso") },
            text = {
                Column {
                    OutlinedTextField(
                        value = currentPages.toString(),
                        onValueChange = { newValue ->
                            newValue.toIntOrNull()?.let { pages ->
                                if (pages in 0..book.totalPages) {
                                    currentPages = pages
                                }
                            }
                        },
                        label = { Text("Páginas leídas") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Total: ${book.totalPages} páginas")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showEditDialog = false
                        onUpdateProgress(book.copy(currentPages = currentPages))
                    }
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}