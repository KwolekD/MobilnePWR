package com.example.mobilnepwr.details_screen

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import com.example.mobilnepwr.R
import com.example.mobilnepwr.TestAppContainer
import com.example.mobilnepwr.ui.course_deatails.CourseDetailsScreen
import com.example.mobilnepwr.ui.course_deatails.CourseDetailsViewModel
import com.example.mobilnepwr.ui.navigation.CourseDetailsDestination
import com.example.mobilnepwr.ui.navigation.ScaffoldViewModel
import io.mockk.every
import io.mockk.mockk
import onNodeWithStringId
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.format.DateTimeFormatter

class TestScaffoldViewModel : ScaffoldViewModel() {
    override fun updateState(
        showFab: Boolean,
        iconFab: ImageVector,
        title: String,
        onNavigationIconClick: () -> Unit,
        navigationIcon: ImageVector,
        enableGestures: Boolean
    ) {

    }
}

class DetailsScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var testContainer: TestAppContainer
    private lateinit var viewModel: CourseDetailsViewModel
    private val savedStateHandle = mockk<SavedStateHandle>()
    private val scaffoldViewModel = mockk<ScaffoldViewModel>()

    @Before
    fun setUp() {
        testContainer = TestAppContainer(composeTestRule.activity)
        testContainer.fillTestDataBase()

        every { savedStateHandle.get<Int>(CourseDetailsDestination.courseIdArg) } returns 1
//        every { scaffoldViewModel.updateState(any()) } just runs
        viewModel = CourseDetailsViewModel(
            savedStateHandle = savedStateHandle,
            courseRepository = testContainer.coursesRepository,
            notesRepository = testContainer.notesRepository,
            deadlineRepository = testContainer.deadlinesRepository,
            dateRepository = testContainer.datesRepository,
            imageRepository = testContainer.imagesRepository
        )
        composeTestRule.setContent {
            CourseDetailsScreen(
                viewModel = viewModel,
                setFabOnClick = {},
                navigateToAddDeadline = {},
                navigateToAddNote = {},
                navigateToEditNote = {},
                navigateToEditDeadline = {},
                contentPadding = PaddingValues.Absolute(),
                scaffoldViewModel = TestScaffoldViewModel()

            )
        }
    }

    @Test
    fun tabOnStart_detailsShown() {
        composeTestRule.waitUntil {
            composeTestRule.onNodeWithText(viewModel.courseUiState.value.courseDetails.name)
                .isDisplayed()
        }

        composeTestRule.onNodeWithText("Szczegóły")
            .assertExists()
            .assertIsSelected()

        composeTestRule.onNodeWithText("Notatki")
            .assertExists()
            .assertIsNotSelected()

        val courseDetails = viewModel.courseUiState.value.courseDetails

        composeTestRule.onNodeWithText(courseDetails.name).assertExists()
        composeTestRule.onNodeWithText("Wykład").assertExists()
        composeTestRule.onNodeWithText(courseDetails.hall).assertExists()
        composeTestRule.onNodeWithText(courseDetails.building).assertExists()
        composeTestRule.onNodeWithText(courseDetails.address).assertExists()
    }

    @Test
    fun tabOnNotes_notesShown() {
        composeTestRule.waitUntil {
            composeTestRule.onNodeWithText(viewModel.courseUiState.value.courseDetails.name)
                .isDisplayed()
        }
        composeTestRule.onNodeWithText("Notatki")
            .performClick()

        composeTestRule.onNodeWithStringId(R.string.notes_tab_title)
        val notes = viewModel.courseUiState.value.notesList

        for (note in notes) {
            composeTestRule.onNodeWithText(note.title).assertExists()
            if (note.content.length > 90) {
                composeTestRule.onNodeWithText(note.contentShort)
                    .assertExists()
                    .performClick()
                    .assertDoesNotExist()
                composeTestRule.onNodeWithText(note.content)
                    .assertExists()

            } else {
                composeTestRule.onNodeWithText(note.content)
                    .assertExists()
            }
        }


    }


    @Test
    fun deadlinesTab_deadlinesShown() {
        composeTestRule.waitUntil {
            composeTestRule.onNodeWithText(viewModel.courseUiState.value.courseDetails.name)
                .isDisplayed()
        }

        composeTestRule.onNodeWithText("Terminy")
            .assertExists()
            .performClick()
        composeTestRule.onNodeWithStringId(R.string.deadlines_tab_title)
        val deadlines = viewModel.courseUiState.value.deadlinesList

        for (deadline in deadlines) {
            composeTestRule.onNodeWithText(deadline.title).assertExists()
            composeTestRule.onNodeWithText(deadline.description).assertExists()
            composeTestRule.onNodeWithText(deadline.date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .assertExists()


        }
    }

    @Test
    fun dateTab_datesShown() {
        composeTestRule.waitUntil {
            composeTestRule.onNodeWithText(viewModel.courseUiState.value.courseDetails.name)
                .isDisplayed()
        }

        composeTestRule.onNodeWithText("Obecność")
            .assertExists()
            .performClick()

        val dates = viewModel.courseUiState.value.datesList


        for (date in dates) {
            composeTestRule.onNodeWithStringId(R.string.dates_tab_title)
            composeTestRule.onNodeWithText(date.date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .assertExists()

            composeTestRule.onNodeWithTag("checkbox${date.dateId}")
                .assertExists()
                .assertIsEnabled()

            if (date.attendance) {
                composeTestRule.onNodeWithTag("checkbox${date.dateId}")
                    .assertIsOn()
            } else {
                composeTestRule.onNodeWithTag("checkbox${date.dateId}")
                    .assertIsNotSelected()
                    .assertIsOff()
            }
        }
    }

    @Test
    fun photosTab_photosShown() {
        composeTestRule.waitUntil {
            composeTestRule.onNodeWithText(viewModel.courseUiState.value.courseDetails.name)
                .isDisplayed()
        }

        composeTestRule.onNodeWithText("Zdjęcia")
            .assertExists()
            .performClick()

        composeTestRule.onNodeWithStringId(R.string.photos_tab_title)
            .assertExists()

    }


}