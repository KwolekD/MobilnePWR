package com.example.mobilnepwr.course_details_tests

import androidx.lifecycle.SavedStateHandle
import com.example.mobilnepwr.MainCoroutineRule
import com.example.mobilnepwr.data.notes.Note
import com.example.mobilnepwr.data.notes.NoteRepository
import com.example.mobilnepwr.ui.course_deatails.NoteDetails
import com.example.mobilnepwr.ui.course_deatails.note.EditNoteViewModel
import com.example.mobilnepwr.ui.navigation.EditNoteDestination
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockkClass
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class NoteEditViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val noteRepository = mockkClass(NoteRepository::class)

    private val savedStateHandle = mockkClass(SavedStateHandle::class)

    private lateinit var viewModel: EditNoteViewModel

    private val notes = listOf(
        NoteDetails(
            courseId = 0,
            noteId = 0,
            title = "Test title 1",
            content = "Test description 1",
            date = LocalDate.of(2023, 1, 1)
        ),
        NoteDetails(
            courseId = 0,
            noteId = 1,
            title = "Test title 2",
            content = "Test description 2",
            date = LocalDate.of(2023, 2, 1)
        )
    )

    @Before
    fun setUp() {
        every { noteRepository.getItemStream(0) } returns flowOf(
            Note(
                noteId = 0,
                courseId = 0,
                title = "Test title 1",
                content = "Test description 1",
                date = LocalDate.of(2023, 1, 1)
            )
        )
        every { noteRepository.getItemStream(1) } returns flowOf(
            Note(
                noteId = 1,
                courseId = 0,
                title = "Test title 2",
                content = "Test description 2",
                date = LocalDate.of(2023, 2, 1)
            )
        )
    }

    @Test
    fun `innitialized viewModel should update uiState`() {
        every { savedStateHandle.get<Int>(EditNoteDestination.noteIdArg) } returns 0
        viewModel = EditNoteViewModel(savedStateHandle, noteRepository)

        assertEquals(viewModel.noteEditUiState.value.noteDetails, notes[0])
        assertEquals(viewModel.noteEditUiState.value.isEntryValid, true)

        every { savedStateHandle.get<Int>(EditNoteDestination.noteIdArg) } returns 1
        viewModel = EditNoteViewModel(savedStateHandle, noteRepository)

        assertEquals(viewModel.noteEditUiState.value.noteDetails, notes[1])
        assertEquals(viewModel.noteEditUiState.value.isEntryValid, true)

    }

    @Test
    fun `updateNoteDetails should update noteDetails`() {
        every { savedStateHandle.get<Int>(EditNoteDestination.noteIdArg) } returns 0
        viewModel = EditNoteViewModel(savedStateHandle, noteRepository)

        viewModel.updateNoteDetails(
            viewModel.noteEditUiState.value.noteDetails.copy(title = "asd")
        )
        assertEquals(viewModel.noteEditUiState.value.noteDetails, notes[0].copy(title = "asd"))
    }

    @Test
    fun `updateNoteDetails should update isEntryValid`() {
        every { savedStateHandle.get<Int>(EditNoteDestination.noteIdArg) } returns 0
        viewModel = EditNoteViewModel(savedStateHandle, noteRepository)
        val noteDetails = NoteDetails(
            title = "Test title",
            content = "Test description"
        )

        assertTrue(viewModel.noteEditUiState.value.isEntryValid)

        viewModel.updateNoteDetails(noteDetails)
        assertTrue(viewModel.noteEditUiState.value.isEntryValid)

        viewModel.updateNoteDetails(noteDetails.copy(title = ""))
        assertFalse(viewModel.noteEditUiState.value.isEntryValid)
    }


    @Test
    fun `updateNote should call noteRepository updateNote if valid`() = runTest {
        coEvery { noteRepository.updateNote(any()) } just Runs
        every { savedStateHandle.get<Int>(EditNoteDestination.noteIdArg) } returns 0
        viewModel = EditNoteViewModel(savedStateHandle, noteRepository)
        val noteToAdd = NoteDetails(
            title = "Test title",
            content = "Test description"
        )

        viewModel.updateNoteDetails(noteToAdd)
        viewModel.updateNote()

        coVerify { noteRepository.updateNote(any()) }


    }

    @Test
    fun `updateNote should not call noteRepository updateNote if invalid`() = runTest {
        coEvery { noteRepository.updateNote(any()) } just Runs
        every { savedStateHandle.get<Int>(EditNoteDestination.noteIdArg) } returns 0
        viewModel = EditNoteViewModel(savedStateHandle, noteRepository)
        val noteToAdd = NoteDetails(
            title = "",
            content = "Test description"
        )

        viewModel.updateNoteDetails(noteToAdd)
        viewModel.updateNote()

        coVerify(exactly = 0) { noteRepository.insertNote(any()) }


    }

}