package com.tecmilenio.booktrackerevidencia

data class Book(
    val title: String,
    val author: String,
    val totalPages: Int,
    val currentPages: Int = 0, // Nuevo campo
    val genre: String,
    val startDate: String,
    val imageUri: String? = null
) {
    fun getGenresList(): List<String> = genre.split(", ").filter { it.isNotBlank() }
}