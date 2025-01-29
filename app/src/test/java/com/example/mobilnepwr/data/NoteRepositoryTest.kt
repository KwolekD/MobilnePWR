package com.example.mobilnepwr.data

import com.example.mobilnepwr.data.notes.Note
import com.example.mobilnepwr.data.notes.NoteDao
import com.example.mobilnepwr.data.notes.NoteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class NoteRepositoryTest {
    private val noteDao = mockk<NoteDao>()
    private lateinit var noteRepository: NoteRepository

    @Before
    fun setUp() {
        noteRepository = NoteRepository(noteDao)
    }

    @Test
    fun `getItemstream should return flow of note`() {
        val note = Note(
            noteId = 1,
            courseId = 1,
            content = "test",
            title = "asd",
            date = LocalDate.of(2002, 1, 1)
        )
        every { noteDao.getNote(1) } returns flowOf(note)
        noteRepository.getItemStream(1)

        verify { noteDao.getNote(1) }
    }

    @Test
    fun `insertNote should call noteDao insert`() = runTest {
        val note = Note(
            noteId = 1,
            courseId = 1,
            content = "test",
            title = "asd",
            date = LocalDate.of(2002, 1, 1)
        )
        coEvery { noteDao.insertNote(note) } returns Unit
        noteRepository.insertNote(note)
        coVerify { noteDao.insertNote(note) }
    }

    @Test
    fun `deleteNote should call noteDao delete`() = runTest {
        val note = Note(
            noteId = 1,
            courseId = 1,
            content = "test",
            title = "asd",
            date = LocalDate.of(2002, 1, 1)
        )
        coEvery { noteDao.deleteNote(note) } returns Unit
        noteRepository.deleteNote(note)
        coVerify { noteDao.deleteNote(note) }

    }

    @Test
    fun `updateNote should call noteDao update`() = runTest {
        val note = Note(
            noteId = 1,
            courseId = 1,
            content = "test",
            title = "asd",
            date = LocalDate.of(2002, 1, 1)
        )
        coEvery { noteDao.updateNote(note) } returns Unit
        noteRepository.updateNote(note)
        coVerify { noteDao.updateNote(note) }

    }

    @Test
    fun `getNotesByCourseId should return flow of notes`() {
        val notes = listOf(
            Note(
                noteId = 1,
                courseId = 1,
                content = "test",
                title = "asd",
                date = LocalDate.of(2002, 1, 1)
            ),
            Note(
                noteId = 2,
                courseId = 1,
                content = "test",
                title = "asd",
                date = LocalDate.of(2002, 1, 1)
            )
        )

        every { noteDao.getNotesByCourseId(1) } returns flowOf(notes)
        noteRepository.getNotesByCourseId(1)

        verify { noteDao.getNotesByCourseId(1) }
    }
}