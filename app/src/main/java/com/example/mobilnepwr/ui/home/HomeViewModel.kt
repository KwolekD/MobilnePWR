package com.example.mobilnepwr.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilnepwr.data.courses.Course
import com.example.mobilnepwr.data.courses.CourseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

class HomeViewModel(private val repository: CourseRepository): ViewModel() {

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState

    init {
        initializeHomeUiState()
    }

    private fun initializeHomeUiState(startDate: LocalDate = LocalDate.now()) {
        viewModelScope.launch {
            val firstDayOfWeek = getFirstDayOfWeek(startDate)
            val weekDays = (0 until 7).map { offset ->
                val currentDay = firstDayOfWeek.plusDays(offset.toLong())
                val courses = repository.getClassesAtDate(currentDay).first()
                WeekDay(
                    name = currentDay.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                    courses = courses,
                    isExpanded = false
                )
            }
            _homeUiState.value = HomeUiState(weekDays)
        }
    }

    fun onDayClick(index: Int){
        _homeUiState.value = _homeUiState.value.copy(
            weekDays = _homeUiState.value.weekDays.mapIndexed { i, weekDay ->
                if (i == index) {
                    weekDay.copy(isExpanded = !weekDay.isExpanded)
                } else {
                    weekDay
                }
            }
        )
    }

    private fun getFirstDayOfWeek(date: LocalDate): LocalDate {
        val weekFields = WeekFields.of(Locale.getDefault())
        return date.with(weekFields.dayOfWeek(), 1)
    }


}



data class HomeUiState(
    val weekDays: List<WeekDay> = listOf(),
)



data class WeekDay(
    val name: String = "",
    val courses: List<Course> = listOf(),
    val isExpanded: Boolean = false
)


