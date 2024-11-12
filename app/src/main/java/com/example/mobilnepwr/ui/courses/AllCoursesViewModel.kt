package com.example.mobilnepwr.ui.courses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilnepwr.data.courses.Course
import com.example.mobilnepwr.data.courses.CourseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.plus

class AllCoursesViewModel(repository: CourseRepository):ViewModel(){
    val courseUiState: StateFlow<CourseUiState> =
        repository.getAllItemsStream().map {CourseUiState(it)}
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILIS),
                initialValue = CourseUiState()
            )
    companion object {
        private const val  TIMEOUT_MILIS = 5_000L
    }
}

data class CourseUiState(val classesList: List<Course> = listOf())