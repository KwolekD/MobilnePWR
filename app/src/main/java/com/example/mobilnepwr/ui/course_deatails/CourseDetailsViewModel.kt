package com.example.mobilnepwr.ui.course_deatails

import android.provider.ContactsContract.Data
import androidx.lifecycle.ViewModel
import com.example.mobilnepwr.data.courses.Course
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.mobilnepwr.data.courses.CourseRepository
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
    }

     fun selectTab(index: Int) {
        _courseUiState.update { currentState ->
            currentState.copy(selectedTab = index)
        }
    }
}


data class CourseDetailsUiState(
    val courseDetails: CourseDetails = CourseDetails(),
    val selectedTab: Int = 0
)

data class NotesDetails(
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