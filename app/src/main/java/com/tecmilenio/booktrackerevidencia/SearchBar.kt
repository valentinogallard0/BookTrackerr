package com.tecmilenio.booktrackerevidencia

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    searchText: TextFieldValue,
    onSearchTextChanged: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth() // Parámetro añadido con valor por defecto
) {
    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchTextChanged,
        placeholder = { Text("Buscar libro...") },
        modifier = modifier, // Usamos el modifier recibido
        shape = RoundedCornerShape(20.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = Color.White,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        )
    )
}