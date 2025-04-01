package com.tecmilenio.booktrackerevidencia

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val author: String,
    val totalPages: Int,
    val genre: String,
    val startDate: String,
    val imageUri: String? = null // Imagen opcional
)
