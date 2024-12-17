package com.example.mobilnepwr.ui.course_deatails

import android.provider.ContactsContract.Data
import androidx.lifecycle.ViewModel
import com.example.mobilnepwr.data.courses.Course
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.mobilnepwr.data.courses.CourseRepository
import com.example.mobilnepwr.data.notes.Note
import com.example.mobilnepwr.data.notes.OfflineNoteRepository
import com.example.mobilnepwr.ui.navigation.CourseDetailsDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class CourseDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val courseRepository: CourseRepository,
    private val notesRepository: OfflineNoteRepository,
): ViewModel() {

    private val courseId: Int = checkNotNull(savedStateHandle[CourseDetailsDestination.courseIdArg])

    private val _courseUiState: MutableStateFlow<CourseDetailsUiState> =
        MutableStateFlow(CourseDetailsUiState())

    val courseUiState = _courseUiState.asStateFlow()

    init {
        viewModelScope.launch {
            courseRepository.getItemStream(courseId)
                .filterNotNull()
                .collect {
                    course -> _courseUiState.update {
                        it.copy(courseDetails = course.toCourseDetails())
                }
                }
        }

        viewModelScope.launch {
            notesRepository.getAllItemsStream()
                .collect { notes ->
                    val filteredNotes = notes.filter { it.courseId == courseId }
                    _courseUiState.update { currentState ->
                        currentState.copy(
                            notesList = filteredNotes.map { note ->
                                NotesDetails(
                                    noteId = note.noteId,
                                    courseId = note.courseId,
                                    title = note.title,
                                    content = note.content,
                                    date = note.date
                                )
                            }
                        )
                    }
                }
        }

    }

     fun selectTab(index: Int) {
        _courseUiState.update { currentState ->
            currentState.copy(selectedTab = index)
        }
    }

    fun addNote(title: String, content: String) {
        val newNote = Note(
            noteId = 0,
            courseId = courseId,
            title = title,
            content = content,
            date = LocalDate.now()
        )

        viewModelScope.launch {
            notesRepository.insertItem(newNote)
        }
    }


data class CourseDetailsUiState(
    val courseDetails: CourseDetails = CourseDetails(),
    val selectedTab: Int = 0,
    val notesList: List<NotesDetails> = emptyList()
)
    data class NotesDetails(
        val noteId: Int = 0,
        val courseId: Int = 0,
        val title: String = "",
        val content: String = "",
        val date: LocalDate = LocalDate.now()
    )


data class CourseDetails(
    val id: Int = 0,
    val name: String = "",
    val type: String = "",
    val address: String = "",
    val building: String = "",
    val hall: String = ""
)

fun Course.toCourseDetails(): CourseDetails = CourseDetails(
    id = courseId,
    name = name,
    type = type,
    address = address,
    building = building,
    hall = hall
)



    fun NotesDetails.toNotesDetails(): NotesDetails = NotesDetails(
        noteId = this.noteId,
        courseId = this.courseId,
        title = this.title,
        content = this.content,
        date = this.date
    )
}
