package com.example.mobilnepwr.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilnepwr.data.courses.Course
import com.example.mobilnepwr.data.courses.CourseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(repository: CourseRepository): ViewModel() {
    val homeUiState: StateFlow<HomeUiState> =
        repository.getAllItemsStream().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILIS),
                initialValue = HomeUiState()
            )

    companion object {
    private const val  TIMEOUT_MILIS = 5_000L
    }
}

data class HomeUiState(val classesList: List<Course> = listOf())