package com.example.mobilnepwr.data.notes


import kotlinx.coroutines.flow.Flow

class NoteRepository(val noteDao: NoteDao) {
    fun getAllItemsStream(): Flow<List<Note>> = noteDao.getAllNotes()

    fun getItemStream(id: Int): Flow<Note?> = noteDao.getNote(id)

    suspend fun insertNote(item: Note) = noteDao.insertNote(item)

    suspend fun deleteNote(item: Note) = noteDao.deleteNote(item)

    suspend fun updateNote(item: Note) = noteDao.updateNote(item)
    fun getNotesByCourseId(courseId: Int): Flow<List<Note>> =
        noteDao.getNotesByCourseId(courseId)
}