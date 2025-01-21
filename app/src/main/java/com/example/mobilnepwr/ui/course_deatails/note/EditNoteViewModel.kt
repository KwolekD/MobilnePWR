package com.example.mobilnepwr.ui.course_deatails.note

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilnepwr.data.notes.Note
import com.example.mobilnepwr.data.notes.NoteRepository
import com.example.mobilnepwr.ui.course_deatails.NoteDetails
import com.example.mobilnepwr.ui.course_deatails.toNoteDetails
import com.example.mobilnepwr.ui.navigation.EditNoteDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class EditNoteViewModel(
    savedStateHandle: SavedStateHandle,
    private val noteRepository: NoteRepository
) : ViewModel() {
    private val noteId: Int = checkNotNull(savedStateHandle[EditNoteDestination.noteIdArg])

    private val _noteEditUiState = MutableStateFlow(AddNoteUiState())
    val noteEditUiState: StateFlow<AddNoteUiState> = _noteEditUiState.asStateFlow()

    init {
        viewModelScope.launch {
            _noteEditUiState.value = AddNoteUiState(
                noteDetails = noteRepository.getItemStream(noteId)
                    .filterNotNull()
                    .first()
                    .toNoteDetails(),
                isEntryValid = true
            )

        }
    }

    fun updateNoteDetails(noteDetails: NoteDetails) {
        _noteEditUiState.value = _noteEditUiState.value.copy(
            noteDetails = noteDetails,
            isEntryValid = validateInput(noteDetails)
        )
    }


    suspend fun updateNote() {
        viewModelScope.launch {
            noteRepository.updateNote(
                _noteEditUiState.value.noteDetails.toNote()
            )
        }
    }

    fun validateInput(noteDetails: NoteDetails): Boolean {
        return with(noteDetails) {
            title.isNotBlank() && content.isNotBlank()
        }
    }
}

fun NoteDetails.toNote(): Note = Note(
    noteId = noteId,
    courseId = courseId,
    title = title,
    content = content,
    date = date,
)