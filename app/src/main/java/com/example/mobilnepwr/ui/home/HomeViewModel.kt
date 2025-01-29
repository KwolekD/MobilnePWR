package com.example.mobilnepwr.ui.home

import androidx.compose.foundation.lazy.LazyListState
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
    private val dateRepository: DateRepository,
    private val dateProvider: () -> LocalDate = { LocalDate.now() },
) : ViewModel() {
    private val _homeUiState = MutableStateFlow(HomeUiState())
    val day = MutableStateFlow(dateProvider())
    val lazyListState = LazyListState()
    val homeUiState: StateFlow<HomeUiState> = _homeUiState

    init {
        initializeHomeUiState(startDate = day.value)
    }


    private fun initializeHomeUiState(startDate: LocalDate = dateProvider(), animOption: Int = 0) {
        viewModelScope.launch {
            val firstDayOfWeek = getFirstDayOfWeek(startDate)
            val weekDays = (0 until 7).map { offset ->
                val currentDay = firstDayOfWeek.plusDays(offset.toLong())
                val courses = courseRepository.getCoursesWithDateDetails(currentDay).first()
                WeekDay(
                    name = currentDay.dayOfWeek.getDisplayName(
                        TextStyle.FULL,
                        Locale("pl", "PL")
                    ),
                    courses = courses,
                    date = currentDay.toString()
                )
            }
            _homeUiState.value = HomeUiState(weekDays, animOption, weekDays.map {
                dateProvider().dayOfWeek.getDisplayName(
                    TextStyle.FULL,
                    Locale("pl", "PL")
                ) == it.name && dateProvider() == day.value
            })
        }
    }


    fun onDayClick(index: Int) {
        _homeUiState.value = _homeUiState.value.copy(
            animOption = 0,
            weekDays = _homeUiState.value.weekDays,
            isExpandedList = _homeUiState.value.isExpandedList.mapIndexed { i, isExpanded ->
                if (i == index) !isExpanded else isExpanded
            }
        )
    }

    fun nextWeek() {
        viewModelScope.launch {
            if (dateRepository.getGreaterThan(day.value).first().isNotEmpty()) {
                day.value = day.value.plusWeeks(1)
                initializeHomeUiState(day.value, 1)
            }
        }
    }

    fun previousWeek() {
        viewModelScope.launch {
            if (dateRepository.getLessThan(day.value).first().isNotEmpty()) {
                day.value = day.value.minusWeeks(1)
                initializeHomeUiState(day.value, 2)
            }
        }
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
    val animOption: Int = 0,
    val isExpandedList: List<Boolean> = listOf()
)


data class WeekDay(
    val name: String = "",
    val date: String = "",
    val courses: List<CourseWithDateDetails> = listOf()
)

fun CourseWithDateDetails.toDate(): Date = Date(
    courseId = courseId,
    date = date,
    attendance = attendance,
    startTime = startTime,
    endTime = endTime,
    dateId = dateId,
)




