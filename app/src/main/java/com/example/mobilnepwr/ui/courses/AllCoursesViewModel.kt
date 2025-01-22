package com.example.mobilnepwr.ui.courses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilnepwr.data.courses.CourseRepository
import com.example.mobilnepwr.ui.course_deatails.CourseDetails
import com.example.mobilnepwr.ui.course_deatails.toCourseDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AllCoursesViewModel(repository: CourseRepository) : ViewModel() {
    private val _courseUiState = MutableStateFlow(CourseUiState())
    val courseUiState: StateFlow<CourseUiState> = _courseUiState

    init {
        viewModelScope.launch {
            _courseUiState.update {
                CourseUiState(repository.getAllItemsStream().first().map { it.toCourseDetails() })

            }
        }
    }
}

data class CourseUiState(val classesList: List<CourseDetails> = listOf())