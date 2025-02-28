package com.tecmilenio.booktrackerevidencia

data class Book(
    val title: String,
    val author: String,
    val totalPages: Int,
    val genre: String,
    val startDate: String,
    val imageUri: String? = null // Imagen opcional
)

