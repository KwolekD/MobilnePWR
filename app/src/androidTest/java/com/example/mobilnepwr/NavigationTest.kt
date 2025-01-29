package com.example.mobilnepwr

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mobilnepwr.ui.navigation.AllCoursesDestination
import com.example.mobilnepwr.ui.navigation.CourseDetailsDestination
import com.example.mobilnepwr.ui.navigation.HomeDestination
import com.example.mobilnepwr.ui.navigation.ImportDestination
import io.mockk.every
import io.mockk.mockkStatic
import onNodeWithStringId
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class NavigationTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: TestNavHostController
    private lateinit var testContainer: TestAppContainer

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>() as MobilnePWRApplication
        mockkStatic(LocalDate::class)
        every { LocalDate.now() } returns LocalDate.of(2025, 1, 20)

        testContainer = TestAppContainer(context)
        testContainer.fillTestDataBase()
        context.setTestContainer(testContainer)
        composeTestRule.setContent {
            navController = TestNavHostController(context).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            MobilnePWRApp(navController = navController)
        }

    }

    @Test
    fun start() {
        navController.assertCurrentRouteName(HomeDestination.route)
    }

    @Test
    fun import() {
        composeTestRule.onNodeWithContentDescription("Navigation icon").performClick()
        composeTestRule.onNodeWithStringId(R.string.import_title).performClick()
        navController.assertCurrentRouteName(ImportDestination.route)
    }

    @Test
    fun allCourses() {
        composeTestRule.onNodeWithContentDescription("Navigation icon").performClick()
        composeTestRule.onNodeWithStringId(R.string.all_courses_title).performClick()
        navController.assertCurrentRouteName(AllCoursesDestination.route)
    }

    @Test
    fun toCourseDetailsFromHome() {
        composeTestRule.waitUntil {
            composeTestRule.onNodeWithText("Math").isDisplayed()
        }
        composeTestRule.onNodeWithText("Math").performClick()
        navController.assertCurrentRouteName(CourseDetailsDestination.routeWithArgs)

    }

    @Test
    fun toCourseDetailsFromAllCourses() {
        composeTestRule.onNodeWithContentDescription("Navigation icon").performClick()
        composeTestRule.onNodeWithStringId(R.string.all_courses_title).performClick()

        composeTestRule.waitUntil {
            composeTestRule.onNodeWithText("Math").isDisplayed()
        }
        composeTestRule.onNodeWithText("Math").performClick()
        navController.assertCurrentRouteName(CourseDetailsDestination.routeWithArgs)
    }

}