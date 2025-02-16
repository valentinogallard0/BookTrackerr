package com.tecmilenio.booktrackerevidencia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
            verticalArrangement = Arrangement.Center
        ) {
            // Título de la pantalla
            Text(
                text = "Book Tracker",
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Descripción o mensaje breve
            Text(
                text = "¡Bienvenido a la app para hacer seguimiento de libros",
                style = MaterialTheme.typography.bodyLarge
            )

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
