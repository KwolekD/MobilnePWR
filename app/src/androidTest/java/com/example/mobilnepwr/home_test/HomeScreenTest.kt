package com.example.mobilnepwr.home_test

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.mobilnepwr.R
import com.example.mobilnepwr.TestAppContainer
import com.example.mobilnepwr.database.TestData
import com.example.mobilnepwr.ui.home.HomeScreen
import com.example.mobilnepwr.ui.home.HomeViewModel
import onNodeWithContentDesctiption
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private var courseClicked: Int = -1

    private lateinit var testContainer: TestAppContainer
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        testContainer = TestAppContainer(composeTestRule.activity)
        testContainer.fillTestDataBase()

        viewModel = HomeViewModel(
            courseRepository = testContainer.coursesRepository,
            dateRepository = testContainer.datesRepository,
            dateProvider = TestData.dayProvider
        )
        composeTestRule.setContent {
            HomeScreen(
                viewModel = viewModel,
                contentPadding = PaddingValues(),
                navigateToCourseDetails = { courseClicked = it }
            )
        }

    }

    @Test
    fun testOnDayClick() {
        composeTestRule.waitUntil { composeTestRule.onNodeWithText("Poniedziałek").isDisplayed() }
        composeTestRule.onNodeWithText("Math").assertExists()
        composeTestRule.onNodeWithText("Poniedziałek")
            .assertExists()
            .performClick()
        composeTestRule.onNodeWithText("Wtorek").assertExists()
        composeTestRule.onNodeWithText("Środa").assertExists()
        composeTestRule.onNodeWithText("Czwartek").assertDoesNotExist()
        composeTestRule.onNodeWithText("Wtorek").performClick()
        composeTestRule.onNodeWithText("Polish").assertExists()
        composeTestRule.onNodeWithText("Środa").performClick()
        composeTestRule.onNodeWithText("Wtorek").performClick()
        composeTestRule.onNodeWithText("English").assertExists()
        composeTestRule.onNodeWithText("Math").assertDoesNotExist()

    }

    @Test
    fun testOnCourseClick() {
        composeTestRule.waitUntil { composeTestRule.onNodeWithText("Poniedziałek").isDisplayed() }
        composeTestRule.onNodeWithText("Math")
            .assertExists()
            .performClick()

        assertEquals(TestData.courses[0].courseId, courseClicked)

        composeTestRule.onNodeWithText("Wtorek").performClick()
        composeTestRule.onNodeWithText("English").assertDoesNotExist()
        composeTestRule.onNodeWithText("Polish")
            .assertExists()
            .performClick()
        assertEquals(TestData.courses[1].courseId, courseClicked)

    }

    @Test
    fun testClickCheckBox() {
        composeTestRule.waitUntil { composeTestRule.onNodeWithText("Poniedziałek").isDisplayed() }
        composeTestRule.onNodeWithText("Math").assertExists()
        composeTestRule.onNodeWithTag("checkbox0")
            .assertExists()
            .performClick()
            .assertIsOff()
            .performClick()
            .assertIsOn()
    }

    @Test
    fun testNextWeek() {
        composeTestRule.waitUntil {
            composeTestRule.onNodeWithText("Poniedziałek").isDisplayed()
        }
        composeTestRule.onNodeWithContentDesctiption(R.string.next_week_desc)
            .assertExists()
            .performClick()

        composeTestRule.waitUntil {
            composeTestRule.onNodeWithText("Poniedziałek").isNotDisplayed()
        }
        composeTestRule.onNodeWithText("Środa").assertExists()
        composeTestRule.onNodeWithText("Poniedziałek").assertDoesNotExist()

        composeTestRule.onNodeWithContentDesctiption(R.string.next_week_desc)
            .assertExists()
            .performClick()

        composeTestRule.onNodeWithText("Środa").assertExists()
        composeTestRule.onNodeWithText("Poniedziałek").assertDoesNotExist()
    }

    @Test
    fun testPreviousWeek() {
        composeTestRule.waitUntil {
            composeTestRule.onNodeWithContentDesctiption(R.string.prev_week_desc).isDisplayed()
        }

        composeTestRule.onNodeWithContentDesctiption(R.string.prev_week_desc)
            .assertExists()
            .performClick()

        composeTestRule.waitUntil {
            composeTestRule.onNodeWithText("Poniedziałek").isNotDisplayed()
        }

        composeTestRule.onNodeWithText("Wtorek").assertExists()
        composeTestRule.onNodeWithText("Poniedziałek").assertDoesNotExist()
        composeTestRule.onNodeWithContentDesctiption(R.string.prev_week_desc)
            .assertExists()
            .performClick()
        composeTestRule.onNodeWithText("Wtorek").assertExists()
        composeTestRule.onNodeWithText("Poniedziałek").assertDoesNotExist()

    }


}