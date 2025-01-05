package com.example.mobilnepwr.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilnepwr.data.courses.CourseRepository
import com.example.mobilnepwr.data.courses.CourseWithDateDetails
import com.example.mobilnepwr.data.dates.Date
import com.example.mobilnepwr.data.dates.DateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

class HomeViewModel(
    private val courseRepository: CourseRepository,
    private val dateRepository: DateRepository
) : ViewModel() {
    private val day = MutableStateFlow(LocalDate.now())
    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState

    init {
        initializeHomeUiState(day.value)
    }

    private fun initializeHomeUiState(startDate: LocalDate = LocalDate.now(), animOption: Int = 0) {
        viewModelScope.launch {
            val firstDayOfWeek = getFirstDayOfWeek(startDate)
            val weekDays = (1 until 6).map { offset ->
                val currentDay = firstDayOfWeek.plusDays(offset.toLong())
                val courses = courseRepository.getCoursesWithDateDetails(currentDay).first()
                WeekDay(
                    name = currentDay.dayOfWeek.getDisplayName(
                        TextStyle.FULL,
                        Locale("pl", "PL")
                    ),
                    courses = courses,
                    isExpanded = false
                )
            }
            _homeUiState.value = HomeUiState(weekDays, animOption)
        }
    }


    fun onDayClick(index: Int) {
        _homeUiState.value = _homeUiState.value.copy(
            animOption = 0,
            weekDays = _homeUiState.value.weekDays.mapIndexed { i, weekDay ->
                if (i == index) {
                    weekDay.copy(isExpanded = !weekDay.isExpanded)
                } else {
                    weekDay
                }
            }
        )
    }

    fun nextWeek() {
        day.value = day.value.plusWeeks(1)
        initializeHomeUiState(day.value, 1)
    }

    fun previousWeek() {
        day.value = day.value.minusWeeks(1)
        initializeHomeUiState(day.value, 2)
    }

    fun clickCheckBox(courseWithDateDetails: CourseWithDateDetails) {
        viewModelScope.launch {
            dateRepository.updateDate(
                courseWithDateDetails.copy(attendance = !courseWithDateDetails.attendance).toDate()
            )
        }

        _homeUiState.value =
            _homeUiState.value.copy(weekDays = _homeUiState.value.weekDays.map { weekDay ->
                weekDay.copy(
                    courses = weekDay.courses.map { course ->
                        if (course.dateId == courseWithDateDetails.dateId) {
                            course.copy(attendance = !course.attendance)
                        } else {
                            course
                        }
                    })
            })
    }

    private fun getFirstDayOfWeek(date: LocalDate): LocalDate {
        val weekFields = WeekFields.of(Locale.getDefault())
        return date.with(weekFields.dayOfWeek(), 1)
    }


}


data class HomeUiState(
    val weekDays: List<WeekDay> = listOf(),
    val animOption: Int = 0
)


data class WeekDay(
    val name: String = "",
    val courses: List<CourseWithDateDetails> = listOf(),
    val isExpanded: Boolean = false
)

fun CourseWithDateDetails.toDate(): Date = Date(
    courseId = courseId,
    date = date,
    attendance = attendance,
    startTime = startTime,
    endTime = endTime,
    dateId = dateId,
)




