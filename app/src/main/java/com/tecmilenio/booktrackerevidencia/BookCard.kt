package com.tecmilenio.booktrackerevidencia

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.tecmilenio.booktrackerevidencia.ui.theme.BookTrackerEvidenciaTheme

@Composable
fun BookCard(book: Book) {
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
                .padding(1.dp)
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
                        .fillMaxSize()
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

@Preview(showBackground = true)
@Composable
fun BookCardPreview() {
    BookTrackerEvidenciaTheme {
        BookCard(
            book = Book(
                title = "Cien años de soledad",
                author = "Gabriel García Márquez",
                totalPages = 432,
                genre = "Novela",
                startDate = "2023-10-01",
                imageUri = null
            )
        )
    }
}