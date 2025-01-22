package com.example.mobilnepwr.all_courses_tests

import com.example.mobilnepwr.MainCoroutineRule
import com.example.mobilnepwr.data.courses.CourseRepository
import com.example.mobilnepwr.ui.courses.AllCoursesViewModel
import io.mockk.every
import io.mockk.mockkClass
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class AllCoursesViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val courseRepository = mockkClass(CourseRepository::class)

    private lateinit var viewModel: AllCoursesViewModel

    @Test
    fun `initializeCourseUiState should populate classesList correctly`() = runTest {
        every { courseRepository.getAllItemsStream() } returns flowOf(AllCoursesViewModelTestData.courses1)
        viewModel = AllCoursesViewModel(courseRepository)

        val classesList = viewModel.courseUiState.value.classesList
        assertEquals(3, classesList.size)
    }

    @Test
    fun `initializeCourseUiState should populate classesList with empty list if repository returns empty list`() {
        every { courseRepository.getAllItemsStream() } returns flowOf(AllCoursesViewModelTestData.courses2)
        viewModel = AllCoursesViewModel(courseRepository)

        val classesList = viewModel.courseUiState.value.classesList
        assertEquals(0, classesList.size)

    }


}