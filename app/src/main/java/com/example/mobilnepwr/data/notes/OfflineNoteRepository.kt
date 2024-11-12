package com.example.mobilnepwr.data.notes


import kotlinx.coroutines.flow.Flow

class OfflineNoteRepository(val noteDao: NoteDao){
    fun getAllItemsStream(): Flow<List<Note>> = noteDao.getAllNotes()

    fun getItemStream(id: Int): Flow<Note?> = noteDao.getNote(id)

    suspend fun insertItem(item: Note) = noteDao.insertNote(item)

    suspend fun deleteItem(item: Note) = noteDao.deleteNote(item)

    suspend fun updateItem(item: Note) = noteDao.updateNote(item)
}