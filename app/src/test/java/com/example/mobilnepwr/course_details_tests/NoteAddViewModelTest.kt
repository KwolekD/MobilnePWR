package com.example.mobilnepwr.course_details_tests

import androidx.lifecycle.SavedStateHandle
import com.example.mobilnepwr.MainCoroutineRule
import com.example.mobilnepwr.data.notes.NoteRepository
import com.example.mobilnepwr.ui.course_deatails.NoteDetails
import com.example.mobilnepwr.ui.course_deatails.note.AddNoteViewModel
import com.example.mobilnepwr.ui.navigation.AddNoteDestination
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockkClass
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NoteAddViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val noteRepository = mockkClass(NoteRepository::class)

    private val savedStateHandle = mockkClass(SavedStateHandle::class)

    private lateinit var viewModel: AddNoteViewModel

    @Before
    fun setUp() {
        every { savedStateHandle.get<Int>(AddNoteDestination.courseIdArg) } returns 0
        viewModel = AddNoteViewModel(savedStateHandle, noteRepository)
    }

    @Test
    fun `updateUiState should update noteDetails`() {
        val noteDetails = NoteDetails(
            title = "Test title",
            content = "Test description"
        )
        viewModel.updateUiState(
            noteDetails
        )
        assertEquals(viewModel.uiState.value.noteDetails, noteDetails)
    }

    @Test
    fun `updateUiState should update isEntryValid`() {
        val noteDetails = NoteDetails(
            title = "Test title",
            content = "Test description"
        )

        assertFalse(viewModel.uiState.value.isEntryValid)

        viewModel.updateUiState(noteDetails)
        assertTrue(viewModel.uiState.value.isEntryValid)

        viewModel.updateUiState(noteDetails.copy(title = ""))
        assertFalse(viewModel.uiState.value.isEntryValid)
    }


    @Test
    fun `saveNote should call noteRepository insertNote if valid`() = runTest {
        coEvery { noteRepository.insertNote(any()) } just Runs
        val noteToAdd = NoteDetails(
            title = "Test title",
            content = "Test description"
        )

        viewModel.updateUiState(noteToAdd)
        viewModel.saveNote()

        coVerify { noteRepository.insertNote(any()) }


    }

    @Test
    fun `saveNote should not call noteRepository insertNote if invalid`() = runTest {
        coEvery { noteRepository.insertNote(any()) } just Runs
        val noteToAdd = NoteDetails(
            title = "",
            content = "Test description"
        )

        viewModel.updateUiState(noteToAdd)
        viewModel.saveNote()

        coVerify(exactly = 0) { noteRepository.insertNote(any()) }


    }

}