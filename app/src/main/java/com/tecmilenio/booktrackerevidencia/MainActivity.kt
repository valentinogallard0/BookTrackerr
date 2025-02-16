package com.tecmilenio.booktrackerevidencia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

@Composable
fun MainScreen() {
    var items by remember { mutableStateOf(listOf<String>()) } // Lista para almacenar los cuadros

    // Contenedor principal
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Título de la pantalla
            Text(
                text = "Book Tracker Prueba commit",
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón para agregar un nuevo cuadro
            Button(
                onClick = {
                    // Agregar un nuevo cuadro (elemento) al hacer clic
                    items = items + "Cuadro ${items.size + 1}"
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar Cuadro")
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Rejilla de cuadros que se agregan dinámicamente
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 100.dp), // El tamaño mínimo de los cuadros
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(1.dp),
                verticalArrangement = Arrangement.spacedBy(50.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(items.size) { index ->
                    Box(
                        modifier = Modifier
                            .size(170.dp)
                            .padding(2.dp)
                            .background(MaterialTheme.colorScheme.primary)

                    ) {
                        Text(
                            text = items[index],
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
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

