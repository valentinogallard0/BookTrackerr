package com.tecmilenio.booktrackerevidencia

import android.app.DatePickerDialog
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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

    var errorMessage by remember { mutableStateOf<String?>(null) }

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
                OutlinedTextField(
                    value = genre,
                    onValueChange = { genre = it },
                    label = { Text("Género") },
                    isError = genre.isEmpty() && errorMessage != null
                )

                // Campo de fecha con DatePicker
                OutlinedTextField(
                    value = startDate,
                    onValueChange = { startDate = it },
                    label = { Text("Fecha de inicio") },
                    isError = startDate.isEmpty() && errorMessage != null,
                    readOnly = true,  // Hacemos el campo de solo lectura
                    modifier = Modifier.clickable { datePickerDialog.show() }  // Al hacer clic se abre el DatePicker
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