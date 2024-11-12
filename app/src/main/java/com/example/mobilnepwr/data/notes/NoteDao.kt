package com.example.mobilnepwr.data.notes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mobilnepwr.data.images.Image
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM notes WHERE noteId = :noteId")
    fun getNote(noteId: Int): Flow<Note>


    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<Note>>
}