package com.example.mobilnepwr.import_test

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.example.mobilnepwr.R
import com.example.mobilnepwr.TestAppContainer
import com.example.mobilnepwr.ui.import.ImportScreen
import com.example.mobilnepwr.ui.import.ImportViewModel
import onNodeWithStringId
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ImportScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var testContainer: TestAppContainer
    private lateinit var viewModel: ImportViewModel


    @Before
    fun setUp() {
        testContainer = TestAppContainer(composeTestRule.activity)

        viewModel = ImportViewModel(
            courseRepository = testContainer.coursesRepository,
            dateRepository = testContainer.datesRepository
        )
        composeTestRule.setContent {
            ImportScreen(
                viewModel = viewModel,
                navigateBack = {},
                setFabOnClick = {},
                contentPadding = PaddingValues.Absolute()
            )
        }
    }

    @Test
    fun importDisableOnBlank() {
        composeTestRule.onNodeWithStringId(R.string.import_button_title)
            .assertExists()
            .assertIsNotEnabled()

        composeTestRule.onNodeWithStringId(R.string.link_label)
            .assertExists()
            .performTextInput("asd")

        composeTestRule.onNodeWithStringId(R.string.link_label)
            .performTextClearance()

        composeTestRule.onNodeWithStringId(R.string.import_button_title)
            .assertExists()
            .assertIsNotEnabled()

    }

    @Test
    fun importEnableOnLink() {
        composeTestRule.onNodeWithStringId(R.string.link_label)
            .assertExists()
            .performTextInput("test")

        composeTestRule.onNodeWithStringId(R.string.import_button_title)
            .assertExists()
            .assertIsEnabled()
    }

    @Test
    fun importWithFullDatabase() {
        testContainer.fillTestDataBase()

        composeTestRule.onNodeWithStringId(R.string.link_label)
            .assertExists()
            .performTextInput("test")

        composeTestRule.onNodeWithStringId(R.string.import_button_title)
            .assertExists()
            .performClick()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onNodeWithStringId(R.string.import_confirm_title).isDisplayed()
        }
        composeTestRule.onNodeWithStringId(R.string.import_confirm_text)
            .assertExists()

        composeTestRule.onNodeWithStringId(R.string.yes_button_title)
            .performClick()

        composeTestRule.waitUntil {
            composeTestRule.onNodeWithStringId(R.string.import_error_title).isDisplayed()
        }

        composeTestRule.onNodeWithStringId(R.string.ok_button_title)
            .performClick()

        composeTestRule.onNodeWithStringId(R.string.import_error_title)
            .assertDoesNotExist()

        composeTestRule.onNodeWithStringId(R.string.import_button_title)
            .assertExists()
    }

    @Test
    fun importWithEmptyDatabase() {
        composeTestRule.onNodeWithStringId(R.string.link_label)
            .assertExists()
            .performTextInput("test")

        composeTestRule.onNodeWithStringId(R.string.import_button_title)
            .assertExists()
            .performClick()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onNodeWithStringId(R.string.import_error_title).isDisplayed()
        }

        composeTestRule.onNodeWithStringId(R.string.import_error_text)
            .assertExists()

        composeTestRule.onNodeWithStringId(R.string.ok_button_title)
            .performClick()

        composeTestRule.onNodeWithStringId(R.string.import_error_title)
            .assertDoesNotExist()

        composeTestRule.onNodeWithStringId(R.string.import_button_title)
            .assertExists()
    }


}