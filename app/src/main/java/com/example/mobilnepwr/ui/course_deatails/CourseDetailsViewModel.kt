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
import kotlinx.coroutines.flow.combine
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

    val courseId: Int = checkNotNull(savedStateHandle[CourseDetailsDestination.courseIdArg])

    private val _courseUiState: MutableStateFlow<CourseDetailsUiState> =
        MutableStateFlow(CourseDetailsUiState())

    val courseUiState = _courseUiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                courseRepository.getItemStream(courseId),
                deadlineRepository.getDeadlinesByCourseId(courseId),
                notesRepository.getNotesByCourseId(courseId),
                dateRepository.getDatesByCourseId(courseId)
            ) { course, deadlines, notes, dates ->
                _courseUiState.update {
                    it.copy(
                        courseDetails = course.toCourseDetails(),
                        deadlinesList = deadlines.map { deadline -> deadline.toDeadlineDetails() },
                        notesList = notes.map { note -> note.toNoteDetails() } ?: emptyList(),
                        datesList = dates.map { date -> date.toDateDetails() } ?: emptyList()
                    )
                }
            }.collect {}
        }
    }

    fun selectTab(index: Int) {
        _courseUiState.update { currentState ->
            currentState.copy(selectedTab = index)
        }
    }

    fun updateCheckBox(date: DateDetails) {
        viewModelScope.launch {
            dateRepository.updateDate(date.copy(attendance = !date.attendance).toDate())
        }
    }
}

data class CourseDetailsUiState(
    val courseDetails: CourseDetails = CourseDetails(),
    val selectedTab: Int = 0,
    val notesList: List<NoteDetails> = emptyList(),
    val deadlinesList: List<DeadlineDetails> = emptyList(),
    val datesList: List<DateDetails> = emptyList()
)

data class NoteDetails(
    val noteId: Int = 0,
    val courseId: Int = 0,
    val title: String = "",
    val content: String = "",
    val date: LocalDate = LocalDate.now()
)


data class CourseDetails(
    val courseId: Int = 0,
    val name: String = "",
    val type: String = "",
    val address: String = "",
    val building: String = "",
    val hall: String = ""
)

data class DeadlineDetails(
    val courseId: Int = 0,
    val title: String = "",
    val description: String = "",
    val date: LocalDate = LocalDate.now()
)

data class DateDetails(
    val dateId: Int = 0,
    val date: LocalDate = LocalDate.now(),
    val attendance: Boolean = true,
    val startTime: String = "",
    val endTime: String = "",
    val courseId: Int = 0
)

fun Course.toCourseDetails(): CourseDetails = CourseDetails(
    courseId = courseId,
    name = name,
    type = type,
    address = address,
    building = building,
    hall = hall
)

fun Deadline.toDeadlineDetails(): DeadlineDetails = DeadlineDetails(
    courseId = courseId,
    title = title,
    description = description,
    date = date
)

fun Date.toDateDetails(): DateDetails = DateDetails(
    dateId = dateId,
    date = date,
    attendance = attendance,
    startTime = startTime,
    endTime = endTime,
    courseId = courseId

)

fun DateDetails.toDate(): Date = Date(
    date = date,
    attendance = attendance,
    courseId = courseId,
    startTime = startTime,
    endTime = endTime,
    dateId = dateId
)


fun Note.toNoteDetails(): NoteDetails = NoteDetails(
    title = this.title,
    content = this.content,
    date = this.date
)

