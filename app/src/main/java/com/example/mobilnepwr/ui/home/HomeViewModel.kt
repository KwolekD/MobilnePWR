package com.example.mobilnepwr.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilnepwr.data.courses.Course
import com.example.mobilnepwr.data.courses.CourseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Locale

class HomeViewModel(val repository: CourseRepository): ViewModel() {
    var currentDate: LocalDate = LocalDate.now()
    private val _homeUiState = MutableStateFlow(HomeUiState(
        weekDayButtonList = createWeekDayButtonList()
    ))
    val homeUiState: StateFlow<HomeUiState> = _homeUiState


    // Funkcja tworząca listę przycisków z dniami tygodnia
    private fun createWeekDayButtonList(): List<WeekDayButton> {
        // Tworzymy listę przycisków, jeden dla każdego dnia tygodnia
        return WEEK_DAYS.mapIndexed { index, dayName ->
            // Dla każdego dnia wyliczamy datę
            val dayOfWeek = getFirstDayOfWeek(currentDate).plusDays(index.toLong())
            WeekDayButton(
                name = dayName,
                // Pobieramy klasy dla danego dnia przy użyciu stateIn
                classesList = getClassesForDay(dayOfWeek),
                clicked = false
            )
        }
    }

    // Funkcja do pobierania danych o klasach w postaci Flow
    private fun getClassesForDay(date: LocalDate): StateFlow<List<Course>> {
        return repository.getClassesAtDate(date.toString())
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    }

    companion object {
        // Lista dni tygodnia
        private val WEEK_DAYS: List<String> =
            listOf("Poniedziałek", "Wtorek", "Środa", "Czwartek", "Piątek")
    }
}

fun getFirstDayOfWeek(date: LocalDate): LocalDate {
    // Tworzymy WeekFields dla lokalizacji, w której tydzień zaczyna się od poniedziałku
    val weekFields = WeekFields.of(Locale("pl", "PL"))  // Używamy lokalizacji PL
    return date.with(weekFields.dayOfWeek(), 1)  // 1 oznacza poniedziałek
}

data class HomeUiState(
    val weekDayButtonList: List<WeekDayButton> = listOf(),
    val firstDayOfWeek: LocalDate = getFirstDayOfWeek(LocalDate.now())
)

data class WeekDayButton(
    val name: String = "",
    val classesList: StateFlow<List<Course>> = MutableStateFlow(listOf()),
    val clicked: Boolean = false){

}



