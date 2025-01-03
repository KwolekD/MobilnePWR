package com.example.mobilnepwr.ui.course_deatails.note

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.mobilnepwr.data.notes.Note
import com.example.mobilnepwr.data.notes.NoteRepository
import com.example.mobilnepwr.ui.course_deatails.NoteDetails
import com.example.mobilnepwr.ui.navigation.AddNoteDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddNoteViewModel(
    savedStateHandle: SavedStateHandle,
    private val noteRepository: NoteRepository
) : ViewModel() {
    private val courseId: Int = checkNotNull(savedStateHandle[AddNoteDestination.courseIdArg])

    private val _uiState = MutableStateFlow(AddNoteUiState())
    val uiState: StateFlow<AddNoteUiState> = _uiState.asStateFlow()


    fun updateUiState(noteDetails: NoteDetails) {
        _uiState.value =
            AddNoteUiState(noteDetails = noteDetails, isEntryValid = validateInput())
    }

    suspend fun saveNote() {
        if (_uiState.value.isEntryValid) {
            noteRepository.insertNote(uiState.value.noteDetails.toNote(courseId))
        }

    }

    fun validateInput(): Boolean {
        return with(_uiState.value.noteDetails) {
            title.isNotBlank() && content.isNotBlank()
        }
    }

}

data class AddNoteUiState(
    val noteDetails: NoteDetails = NoteDetails(),
    val showDatePicker: Boolean = false,
    val isEntryValid: Boolean = false
)

fun NoteDetails.toNote(courseId: Int): Note = Note(
    title = title,
    content = content,
    date = date,
    courseId = courseId,
    noteId = 0
)