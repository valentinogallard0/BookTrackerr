package com.tecmilenio.booktrackerevidencia

import android.app.DatePickerDialog
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddBookDialog(
    onDismiss: () -> Unit,
    onBookAdded: (Book) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var totalPages by remember { mutableStateOf("") }
    var selectedGenres by remember { mutableStateOf<List<String>>(emptyList()) }
    var startDate by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<String?>(null) }
    var showGenreDialog by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Lista predefinida de géneros
    val availableGenres = listOf(
        "Novela", "Ciencia Ficción", "Fantasía", "Misterio", "Romance",
        "Thriller", "Terror", "Historia", "Biografía", "Poesía",
        "Ensayo", "Autoayuda", "Infantil", "Juvenil", "Cómic",
        "Académico", "Drama", "Aventura", "Histórico", "Distopía"
    )

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Formato para mostrar la fecha
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    // Configuración del DatePickerDialog
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            startDate = dateFormatter.format(calendar.time)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { imageUri = it.toString() }
    }

    // Diálogo de selección de géneros (versión mejorada)
    if (showGenreDialog) {
        Dialog(onDismissRequest = { showGenreDialog = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Selecciona los géneros",
                            style = MaterialTheme.typography.titleMedium
                        )
                        IconButton(onClick = { showGenreDialog = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn {
                        items(availableGenres) { genre ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedGenres = if (selectedGenres.contains(genre)) {
                                            selectedGenres - genre
                                        } else {
                                            selectedGenres + genre
                                        }
                                    }
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = selectedGenres.contains(genre),
                                    onCheckedChange = null
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = genre)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { showGenreDialog = false },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Aceptar")
                    }
                }
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar Libro") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())  // ¡Este es el cambio clave!
            ) {
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
                    isError = title.isEmpty() && errorMessage != null
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

                // Campo de géneros
                OutlinedTextField(
                    value = if (selectedGenres.isEmpty()) "" else "Géneros seleccionados (${selectedGenres.size})",
                    onValueChange = {},
                    label = { Text("Géneros") },
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.clickable { showGenreDialog = true }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showGenreDialog = true }
                )

                // Chips de géneros - Versión optimizada
                if (selectedGenres.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),  // Altura fija para evitar crecimiento vertical
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(selectedGenres) { genre ->
                            InputChip(
                                selected = true,
                                onClick = {},
                                label = { Text(genre) },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remover",
                                        modifier = Modifier.clickable {
                                            selectedGenres = selectedGenres - genre
                                        }
                                    )
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Campo de fecha
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = startDate,
                        onValueChange = { startDate = it },
                        label = { Text("Fecha de inicio") },
                        readOnly = true,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { datePickerDialog.show() }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                    }
                }

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
                if (title.isEmpty() || author.isEmpty() || totalPages.isEmpty() || selectedGenres.isEmpty() || startDate.isEmpty()) {
                    errorMessage = "Todos los campos son obligatorios"
                } else if (totalPages.toIntOrNull() == null) {
                    errorMessage = "El número de páginas debe ser un valor válido"
                } else {
                    // Convertimos la lista de géneros a un solo string separado por comas
                    val genresString = selectedGenres.joinToString(", ")
                    onBookAdded(Book(title, author, totalPages.toInt(), genresString, startDate, imageUri))
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