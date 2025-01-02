package com.example.mobilnepwr.ui.course_deatails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilnepwr.data.courses.Course
import com.example.mobilnepwr.data.courses.CourseRepository
import com.example.mobilnepwr.data.dates.Date
import com.example.mobilnepwr.data.dates.DateRepository
import com.example.mobilnepwr.data.deadlines.Deadline
import com.example.mobilnepwr.data.deadlines.DeadlineRepository
import com.example.mobilnepwr.data.notes.Note
import com.example.mobilnepwr.data.notes.NoteRepository
import com.example.mobilnepwr.ui.navigation.CourseDetailsDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class CourseDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val courseRepository: CourseRepository,
    private val notesRepository: NoteRepository,
    private val deadlineRepository: DeadlineRepository,
    private val dateRepository: DateRepository,
) : ViewModel() {

    private val courseId: Int = checkNotNull(savedStateHandle[CourseDetailsDestination.courseIdArg])

    private val _courseUiState: MutableStateFlow<CourseDetailsUiState> =
        MutableStateFlow(CourseDetailsUiState())

    val courseUiState = _courseUiState.asStateFlow()

    init {
        viewModelScope.launch {

            courseRepository.getItemStream(courseId)
                .filterNotNull()
                .collect { course ->
                    _courseUiState.update {
                        it.copy(courseDetails = course.toCourseDetails())
                    }
                }

            deadlineRepository.getDeadlinesByCourseId(courseId)
                .filterNotNull()
                .collect { deadlines ->
                    _courseUiState.update {
                        it.copy(deadlinesList = deadlines.map { deadline ->
                            deadline.toDeadlineDetails()
                        })
                    }
                }

            notesRepository.getNotesByCourseId(courseId)
                .filterNotNull()
                .collect { notes ->
                    _courseUiState.update {
                        it.copy(notesList = notes.map { note ->
                            note.toNotesDetails()
                        })
                    }
                }

            dateRepository.getDatesByCourseId(courseId)
                .filterNotNull()
                .collect { dates ->
                    _courseUiState.update {
                        it.copy(datesList = dates.map { date ->
                            date.toDateDetails()
                        })
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
}

data class CourseDetailsUiState(
    val courseDetails: CourseDetails = CourseDetails(),
    val selectedTab: Int = 0,
    val notesList: List<NotesDetails> = emptyList(),
    val deadlinesList: List<DeadlineDetails> = emptyList(),
    val datesList: List<DateDetails> = emptyList()
)

data class NotesDetails(
    val noteId: Int = 0,
    val courseId: Int = 0,
    val title: String = "",
    val content: String = "",
    val date: LocalDate = LocalDate.now()
)


data class CourseDetails(
    val name: String = "",
    val type: String = "",
    val address: String = "",
    val building: String = "",
    val hall: String = ""
)

data class DeadlineDetails(
    val title: String = "",
    val description: String = "",
    val date: LocalDate = LocalDate.now()
)

data class DateDetails(
    val date: LocalDate = LocalDate.now(),
    val attendanceStatus: Int = 1
)

fun Course.toCourseDetails(): CourseDetails = CourseDetails(
    name = name,
    type = type,
    address = address,
    building = building,
    hall = hall
)

fun Deadline.toDeadlineDetails(): DeadlineDetails = DeadlineDetails(
    title = title,
    description = description,
    date = date
)

fun Date.toDateDetails(): DateDetails = DateDetails(
    date = date,
    attendanceStatus = attendanceStatus
)


fun Note.toNotesDetails(): NotesDetails = NotesDetails(
    title = this.title,
    content = this.content,
    date = this.date
)

