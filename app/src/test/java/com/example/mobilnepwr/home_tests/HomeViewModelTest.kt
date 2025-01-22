package com.example.mobilnepwr.home_tests

import com.example.mobilnepwr.MainCoroutineRule
import com.example.mobilnepwr.data.courses.CourseRepository
import com.example.mobilnepwr.data.dates.DateRepository
import com.example.mobilnepwr.ui.home.HomeViewModel
import com.example.mobilnepwr.ui.home.toDate
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockkClass
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class HomeViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val courseRepository = mockkClass(CourseRepository::class)
    private val dateRepository = mockkClass(DateRepository::class)

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        every { courseRepository.getCoursesWithDateDetails(any()) } answers {
            val requestedDate = firstArg<LocalDate>()
            val courses = when (requestedDate) {
                HomeViewModelTestData.day1 -> HomeViewModelTestData.coursesForDay1
                HomeViewModelTestData.day2 -> HomeViewModelTestData.coursesForDay2
                HomeViewModelTestData.day3 -> HomeViewModelTestData.coursesForDay3
                HomeViewModelTestData.day4 -> HomeViewModelTestData.coursesForDay4
                HomeViewModelTestData.day5 -> HomeViewModelTestData.coursesForDay5
                HomeViewModelTestData.day6 -> HomeViewModelTestData.coursesForDay6
                HomeViewModelTestData.day7 -> HomeViewModelTestData.coursesForDay7
                else -> emptyList()
            }
            flowOf(courses)
        }

    }

    @Test
    fun `initializeHomeUiState should populate weekDays correctly`() = runTest {
        val expectedWeekDays = listOf(
            Triple("poniedziałek", 3, HomeViewModelTestData.day1.toString()),
            Triple("wtorek", 0, HomeViewModelTestData.day2.toString()),
            Triple("środa", 1, HomeViewModelTestData.day3.toString()),
            Triple("czwartek", 2, HomeViewModelTestData.day4.toString()),
            Triple("piątek", 1, HomeViewModelTestData.day5.toString()),
            Triple("sobota", 0, HomeViewModelTestData.day6.toString()),
            Triple("niedziela", 0, HomeViewModelTestData.day7.toString())
        )

        testHomeUiStateInitialization(HomeViewModelTestData::day1, expectedWeekDays)
        testHomeUiStateInitialization(HomeViewModelTestData::day7, expectedWeekDays)
    }

    private fun testHomeUiStateInitialization(
        testDateProvider: () -> LocalDate,
        expectedWeekDays: List<Triple<String, Int, String>>
    ) = runTest {
        viewModel = HomeViewModel(courseRepository, dateRepository, testDateProvider)

        val weekDays = viewModel.homeUiState.value.weekDays
        assertEquals(7, weekDays.size)

        expectedWeekDays.forEachIndexed { index, (name, courseCount, date) ->
            val day = weekDays[index]
            assertEquals(name, day.name)
            assertEquals(courseCount, day.courses.size)
            assertEquals(date, day.date)
        }
    }

    @Test
    fun `initializeHomeUiState should populate isExpanded list correctly`() = runTest {
        viewModel =
            HomeViewModel(courseRepository, dateRepository) { HomeViewModelTestData.day1 }
        assertTrue(viewModel.homeUiState.value.isExpandedList.size == 7)
        assertTrue(viewModel.homeUiState.value.isExpandedList[0])

        viewModel = HomeViewModel(courseRepository, dateRepository) { HomeViewModelTestData.day4 }
        assertTrue(viewModel.homeUiState.value.isExpandedList.size == 7)
        assertTrue(viewModel.homeUiState.value.isExpandedList[3])
    }

    @Test
    fun `onDayClick should toggle isExpanded state`() = runTest {
        viewModel = HomeViewModel(courseRepository, dateRepository) { HomeViewModelTestData.day1 }
        viewModel.onDayClick(1)
        viewModel.onDayClick(4)
        viewModel.onDayClick(5)
        viewModel.onDayClick(5)
        viewModel.onDayClick(0)
        val updatedState = viewModel.homeUiState.value
        assertEquals(
            listOf(false, true, false, false, true, false, false),
            updatedState.isExpandedList
        )
    }


    @Test
    fun `clickCheckBox should toggle attendance and update repository`() = runTest {
        coEvery { dateRepository.updateDate(any()) } just Runs
        viewModel = HomeViewModel(courseRepository, dateRepository) { HomeViewModelTestData.day1 }

        viewModel.clickCheckBox(HomeViewModelTestData.coursesForDay1[1])
        viewModel.clickCheckBox(HomeViewModelTestData.coursesForDay3[0])

        val updatedState = viewModel.homeUiState.value
        assertTrue(updatedState.weekDays[0].courses[1].attendance)
        assertFalse(updatedState.weekDays[2].courses[0].attendance)

        coVerify {
            dateRepository.updateDate(
                HomeViewModelTestData.coursesForDay1[1].copy(attendance = true).toDate()
            )
            dateRepository.updateDate(
                HomeViewModelTestData.coursesForDay3[0].copy(attendance = false).toDate()
            )
        }
    }

    @Test
    fun `nextWeek should update day and initializeHomeUiState`() = runTest {
        viewModel = HomeViewModel(courseRepository, dateRepository) { HomeViewModelTestData.day1 }
        viewModel.nextWeek()
        assertEquals(viewModel.day.value, HomeViewModelTestData.day1.plusWeeks(1))
        viewModel.nextWeek()
        assertEquals(viewModel.day.value, HomeViewModelTestData.day1.plusWeeks(2))
    }

    @Test
    fun `previousWeek should update day and initializeHomeUiState`() = runTest {
        viewModel = HomeViewModel(courseRepository, dateRepository) { HomeViewModelTestData.day1 }
        viewModel.previousWeek()
        assertEquals(viewModel.day.value, HomeViewModelTestData.day1.minusWeeks(1))
        viewModel.previousWeek()
        assertEquals(viewModel.day.value, HomeViewModelTestData.day1.minusWeeks(2))
    }
}
