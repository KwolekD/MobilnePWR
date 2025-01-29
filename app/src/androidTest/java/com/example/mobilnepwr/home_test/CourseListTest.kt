package com.example.mobilnepwr.home_test

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.mobilnepwr.data.courses.CourseWithDateDetails
import com.example.mobilnepwr.ui.home.CourseList
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class CourseListTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var courses: List<CourseWithDateDetails>

    @Before
    fun setUp() {
        courses = listOf(
            CourseWithDateDetails(
                courseId = 1,
                name = "Course 1",
                type = "Type 1",
                startTime = "10:00",
                endTime = "12:00",
                date = LocalDate.of(2023, 9, 10),
                attendance = false,
                dateId = 1
            ),
            CourseWithDateDetails(
                courseId = 2,
                name = "Course 2",
                type = "Type 2",
                startTime = "10:12",
                endTime = "12:00",
                date = LocalDate.of(2023, 9, 11),
                attendance = false,
                dateId = 2
            )
        )
    }


    @Test
    fun testCourseList_visible() {
        var clickedCourse: Int? = null
        var clickedCheckBox: CourseWithDateDetails? = null
        composeTestRule.setContent {
            CourseList(
                courseList = courses,
                isExpanded = true,
                onCourseClick = { id -> clickedCourse = id },
                clickCheckBox = { course -> clickedCheckBox = course }
            )

        }
        composeTestRule.onNodeWithText("Course 1").assertExists()
        composeTestRule.onNodeWithText("Course 2").assertExists()
        composeTestRule.onNodeWithText("Type 1").assertExists()
        composeTestRule.onNodeWithText("Type 2").assertExists()
        composeTestRule.onNodeWithText("10:00 - 12:00").assertExists()
        composeTestRule.onNodeWithText("10:12 - 12:00").assertExists()
        composeTestRule.onNodeWithText("Course 1").performClick()
        assert(clickedCourse == 1)

    }

    @Test
    fun testCourseList_not_visible() {
        var clickedCourse: Int? = null
        var clickedCheckBox: CourseWithDateDetails? = null

        composeTestRule.setContent {
            CourseList(
                courseList = courses,
                isExpanded = false,
                onCourseClick = { id -> clickedCourse = id },
                clickCheckBox = { course -> clickedCheckBox = course }
            )
        }
        composeTestRule.onNodeWithText("Course 1").assertDoesNotExist()
        composeTestRule.onNodeWithText("Course 2").assertDoesNotExist()
    }


}