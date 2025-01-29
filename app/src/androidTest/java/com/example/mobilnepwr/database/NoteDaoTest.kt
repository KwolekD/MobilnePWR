package com.example.mobilnepwr.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.mobilnepwr.data.AppDatabase
import com.example.mobilnepwr.data.notes.Note
import com.example.mobilnepwr.data.notes.NoteDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class NoteDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var dao: NoteDao

    private val testCourses = TestData.courses

    private val testNotes = listOf(
        Note(
            noteId = 1,
            courseId = testCourses[0].courseId,
            title = "note1",
            content = "content1",
            date = LocalDate.of(2002, 1, 1)
        ),
        Note(
            noteId = 2,
            courseId = testCourses[0].courseId,
            title = "note2",
            content = "content2",
            date = LocalDate.of(2002, 1, 1)
        ),
        Note(
            noteId = 3,
            courseId = testCourses[1].courseId,
            title = "note3",
            content = "content3",
            date = LocalDate.of(2002, 1, 1)
        )
    )

    @Before
    fun setup() = runTest {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.noteDao()
        database.courseDao().insertAll(testCourses)
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertNote() = runTest {
        dao.insertNote(testNotes[0])
        val note = dao.getNote(testNotes[0].noteId).first()
        assertEquals(testNotes[0], note)

        dao.insertNote(testNotes[1])
        val note2 = dao.getNote(testNotes[1].noteId).first()
        assertEquals(testNotes[1], note2)

    }

    @Test
    fun updateNote() = runTest {
        insertNotes()

        dao.updateNote(testNotes[0].copy(title = "updated"))
        val note = dao.getNote(testNotes[0].noteId).first()
        assertEquals(testNotes[0].copy(title = "updated"), note)

    }

    @Test
    fun deleteNote() = runTest {
        insertNotes()
        dao.deleteNote(testNotes[0])
        val notes = dao.getAllNotes().first()
        assertEquals(2, notes.size)
        assertEquals(testNotes[1], notes[0])


    }

    @Test
    fun getNotesByCourseId() = runTest {
        insertNotes()
        val notes = dao.getNotesByCourseId(testCourses[0].courseId).first()
        assertEquals(2, notes.size)
        assertEquals(testNotes[0], notes[0])
        assertEquals(testNotes[1], notes[1])
    }

    @Test
    fun getNote() = runTest {
        insertNotes()
        val note = dao.getNote(testNotes[0].noteId).first()
        assertEquals(testNotes[0], note)

        val note2 = dao.getNote(testNotes[1].noteId).first()
        assertEquals(testNotes[1], note2)

    }

    @Test
    fun getAllNotes() = runTest {
        insertNotes()
        val notes = dao.getAllNotes().first()
        assertEquals(3, notes.size)
        assertEquals(testNotes, notes)
    }


    private suspend fun insertNotes() {
        for (note in testNotes) {
            dao.insertNote(note)
        }
    }
}