package com.example.mobilnepwr.ui.course_deatails

import androidx.lifecycle.ViewModel
import com.example.mobilnepwr.data.courses.Course
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.mobilnepwr.data.courses.CourseRepository
import com.example.mobilnepwr.ui.navigation.CourseDetailsDestination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CourseDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val courseRepository: CourseRepository,
): ViewModel() {

    private val courseId: Int = checkNotNull(savedStateHandle[CourseDetailsDestination.courseIdArg])

     val courseUiState: StateFlow<CourseDetailsUiState> =
        courseRepository.getItemStream(courseId)
            .filterNotNull()
            .map {
                CourseDetailsUiState(courseDetails = it.toCourseDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = CourseDetailsUiState()
            )
}


data class CourseDetailsUiState(
    val courseDetails: CourseDetails = CourseDetails()
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