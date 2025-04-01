package com.tecmilenio.booktrackerevidencia

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookDao {
    @Query("SELECT * FROM books")
    fun getAll(): LiveData<List<Book>>

    @Insert
    fun insertAll(vararg books: Book)

    @Query("DELETE FROM books")
    fun deleteAll()
}