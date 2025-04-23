package com.tecmilenio.booktrackerevidencia

data class Book(
    val title: String,
    val author: String,
    val totalPages: Int,
    val genre: String, // Sigue siendo un String, pero ahora contendrá géneros separados por coma
    val startDate: String,
    val imageUri: String? = null
) {
    // Método para obtener la lista de géneros
    fun getGenresList(): List<String> {
        return genre.split(", ")
    }
}