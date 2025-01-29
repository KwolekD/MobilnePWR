package com.example.mobilnepwr.home_test

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.mobilnepwr.data.courses.CourseWithDateDetails
import com.example.mobilnepwr.ui.home.DayList
import com.example.mobilnepwr.ui.home.WeekDay
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class DayListTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val daysListFull = listOf(
        WeekDay(
            "monday", "10.09.2023", listOf(
                CourseWithDateDetails(
                    courseId = 1,
                    name = "Course 1",
                    type = "Type 1",
                    startTime = "10:00",
                    endTime = "12:00",
                    date = LocalDate.of(2023, 9, 10),
                    attendance = false,
                    dateId = 1
                )
            )
        ),
        WeekDay(
            "tuesday", "11.09.2023", listOf(
                CourseWithDateDetails(
                    courseId = 2,
                    name = "Course 2",
                    type = "Type 2",
                    startTime = "10:12",
                    endTime = "12:00",
                    date = LocalDate.of(2023, 9, 11),
                    attendance = false,
                    dateId = 2
                ),
                CourseWithDateDetails(
                    courseId = 3,
                    name = "Course 3",
                    type = "Type 3",
                    startTime = "10:12",
                    endTime = "12:00",
                    date = LocalDate.of(2023, 9, 11),
                    attendance = false,
                    dateId = 2
                )
            )
        )
    )

    private val daysListEmpty = listOf(
        WeekDay(
            "monday", "10.09.2023", listOf()
        ),
        WeekDay(
            "tuesday", "11.09.2023", listOf()
        )

    )
    private val daysListOne = listOf(
        WeekDay(
            "monday", "10.09.2023", listOf(
                CourseWithDateDetails(
                    courseId = 1,
                    name = "Course 1",
                    type = "Type 1",
                    startTime = "10:00",
                    endTime = "12:00",
                    date = LocalDate.of(2023, 9, 1),
                    attendance = false,
                    dateId = 1
                )
            )
        ),
        WeekDay(
            "tuesday", "11.09.2023", listOf()
        )
    )

    @Test
    fun testDayList_full_courses() {
        val daysList = daysListFull
        val isExpandedList = daysListFull.map { false }
        var clickedIndex: Int? = null

        composeTestRule.setContent {
            DayList(
                daysList = daysList,
                isExpandedList = isExpandedList,
                onCourseClick = {},
                onDayClick = { clickedIndex = it },
                clickCheckBox = {},
                listState = LazyListState()
            )
        }
        composeTestRule.onNodeWithText("Monday").assertExists()
        composeTestRule.onNodeWithText("Tuesday").assertExists()
        composeTestRule.onNodeWithText("Monday").performClick()
        assertEquals(0, clickedIndex)
        composeTestRule.onNodeWithText("Tuesday").performClick()
        assertEquals(1, clickedIndex)
    }

    @Test
    fun testCourseList_empty_courses() {
        val courseList = daysListEmpty
        var clickedIndex: Int? = null
        composeTestRule.setContent {
            DayList(
                daysList = courseList,
                isExpandedList = courseList.map { false },
                onCourseClick = {},
                onDayClick = { clickedIndex = it },
                clickCheckBox = {},
                listState = LazyListState()
            )
        }
        composeTestRule.onNodeWithText("Monday").assertDoesNotExist()
        composeTestRule.onNodeWithText("Tuesday").assertDoesNotExist()

    }

    @Test
    fun testCourseList_one_course() {
        val courseList = daysListOne
        var clickedIndex: Int? = null

        composeTestRule.setContent {
            DayList(
                daysList = courseList,
                isExpandedList = courseList.map { false },
                onCourseClick = {},
                onDayClick = { clickedIndex = it },
                clickCheckBox = {},
                listState = LazyListState()
            )
        }
        composeTestRule.onNodeWithText("Monday").assertExists()
        composeTestRule.onNodeWithText("Tuesday").assertDoesNotExist()

    }
}