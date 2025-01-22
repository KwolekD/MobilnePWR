package com.example.mobilnepwr.course_details_tests

import androidx.lifecycle.SavedStateHandle
import com.example.mobilnepwr.MainCoroutineRule
import com.example.mobilnepwr.course_details_tests.CourseDetailsViewModelTestData.course1
import com.example.mobilnepwr.course_details_tests.CourseDetailsViewModelTestData.course1Dates
import com.example.mobilnepwr.course_details_tests.CourseDetailsViewModelTestData.course1Deadlines
import com.example.mobilnepwr.course_details_tests.CourseDetailsViewModelTestData.course1Images
import com.example.mobilnepwr.course_details_tests.CourseDetailsViewModelTestData.course1Notes
import com.example.mobilnepwr.course_details_tests.CourseDetailsViewModelTestData.course2
import com.example.mobilnepwr.course_details_tests.CourseDetailsViewModelTestData.course2Dates
import com.example.mobilnepwr.course_details_tests.CourseDetailsViewModelTestData.course2Deadlines
import com.example.mobilnepwr.course_details_tests.CourseDetailsViewModelTestData.course2Images
import com.example.mobilnepwr.course_details_tests.CourseDetailsViewModelTestData.course2Notes
import com.example.mobilnepwr.course_details_tests.CourseDetailsViewModelTestData.course3
import com.example.mobilnepwr.course_details_tests.CourseDetailsViewModelTestData.course3Dates
import com.example.mobilnepwr.course_details_tests.CourseDetailsViewModelTestData.course3Deadlines
import com.example.mobilnepwr.course_details_tests.CourseDetailsViewModelTestData.course3Images
import com.example.mobilnepwr.course_details_tests.CourseDetailsViewModelTestData.course3Notes
import com.example.mobilnepwr.data.courses.Course
import com.example.mobilnepwr.data.courses.CourseRepository
import com.example.mobilnepwr.data.dates.Date
import com.example.mobilnepwr.data.dates.DateRepository
import com.example.mobilnepwr.data.deadlines.Deadline
import com.example.mobilnepwr.data.deadlines.DeadlineRepository
import com.example.mobilnepwr.data.images.Image
import com.example.mobilnepwr.data.images.ImageRepository
import com.example.mobilnepwr.data.notes.Note
import com.example.mobilnepwr.data.notes.NoteRepository
import com.example.mobilnepwr.ui.course_deatails.CourseDetails
import com.example.mobilnepwr.ui.course_deatails.CourseDetailsViewModel
import com.example.mobilnepwr.ui.course_deatails.DateDetails
import com.example.mobilnepwr.ui.course_deatails.DeadlineDetails
import com.example.mobilnepwr.ui.course_deatails.NoteDetails
import com.example.mobilnepwr.ui.course_deatails.toCourseDetails
import com.example.mobilnepwr.ui.course_deatails.toDateDetails
import com.example.mobilnepwr.ui.course_deatails.toDeadlineDetails
import com.example.mobilnepwr.ui.course_deatails.toNoteDetails
import com.example.mobilnepwr.ui.navigation.CourseDetailsDestination
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkClass
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CourseDetailsViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val courseRepository = mockkClass(CourseRepository::class)
    private val notesRepository = mockkClass(NoteRepository::class)
    private val deadlineRepository = mockkClass(DeadlineRepository::class)
    private val dateRepository = mockkClass(DateRepository::class)
    private val imageRepository = mockkClass(ImageRepository::class)

    private val savedStateHandle = mockkClass(SavedStateHandle::class)

    private lateinit var viewModel: CourseDetailsViewModel

    private val courses = listOf(
        TestCourseData(
            course = course1,
            notes = course1Notes,
            deadlines = course1Deadlines,
            dates = course1Dates,
            images = course1Images
        ),
        TestCourseData(
            course = course2,
            notes = course2Notes,
            deadlines = course2Deadlines,
            dates = course2Dates,
            images = course2Images
        ),
        TestCourseData(
            course = course3,
            notes = course3Notes,
            deadlines = course3Deadlines,
            dates = course3Dates,
            images = course3Images
        )
    )

    @Before
    fun setUp() {
        courses.forEach {
            mockRepositories(it)
        }
    }

    @Test
    fun `init should initialize correctly for all courses`() {
        courses.forEach { testData ->
            every { savedStateHandle.get<Int>(CourseDetailsDestination.courseIdArg) } returns testData.course.courseId
            initViewModel()
            assertEquals(viewModel.courseId, testData.course.courseId)
            assertCourseDetails(
                viewModel.courseUiState.value.courseDetails,
                testData.course
            )
            assertNotes(
                viewModel.courseUiState.value.notesList,
                testData.notes
            )
            assertDates(
                viewModel.courseUiState.value.datesList,
                testData.dates
            )
            assertDeadlines(
                viewModel.courseUiState.value.deadlinesList,
                testData.deadlines
            )
            assertImages(
                viewModel.courseUiState.value.imageList,
                testData.images.map { it })
        }
    }

    @Test
    fun `selectTab should update selectedTab and prevSelectedTab`() {
        val testData = courses.first()
        every { savedStateHandle.get<Int>(CourseDetailsDestination.courseIdArg) } returns testData.course.courseId
        initViewModel()
        viewModel.selectTab(1)
        assertEquals(viewModel.courseUiState.value.selectedTab, 1)
        assertEquals(viewModel.courseUiState.value.prevSelectedTab, 0)

        viewModel.selectTab(3)
        assertEquals(viewModel.courseUiState.value.selectedTab, 3)
        assertEquals(viewModel.courseUiState.value.prevSelectedTab, 1)
    }

    @Test
    fun `deleteNote should remove the note from the list`() = runTest {
        val testData = courses.first()
        every { savedStateHandle.get<Int>(CourseDetailsDestination.courseIdArg) } returns testData.course.courseId
        initViewModel()

        val noteToDelete = testData.notes.first()
        viewModel.deleteNote(noteToDelete.toNoteDetails())
        assertNotes(
            viewModel.courseUiState.value.notesList,
            testData.notes.filter { it != noteToDelete })
        coVerify {
            notesRepository.deleteNote(noteToDelete)
        }
    }

    @Test
    fun `deleteDeadline should remove the deadline from the list`() = runTest {
        val testData = courses.first()
        every { savedStateHandle.get<Int>(CourseDetailsDestination.courseIdArg) } returns testData.course.courseId
        initViewModel()

        val deadlineToDelete = testData.deadlines[1]
        viewModel.deleteDeadline(deadlineToDelete.toDeadlineDetails())
        assertDeadlines(
            viewModel.courseUiState.value.deadlinesList,
            testData.deadlines.filter { it != deadlineToDelete })
        coVerify {
            deadlineRepository.deleteDeadline(deadlineToDelete)
        }
    }

    @Test
    fun `deletePhoto should remove the photo from the list`() = runTest {
        val testData = courses.first()
        every { savedStateHandle.get<Int>(CourseDetailsDestination.courseIdArg) } returns testData.course.courseId
        initViewModel()

        val imageToDelete = testData.images.first()
        viewModel.deletePhoto(imageToDelete)
        assertImages(
            viewModel.courseUiState.value.imageList,
            testData.images.filter { it != imageToDelete })
        coVerify {
            imageRepository.deleteItem(imageToDelete)
        }
    }

    @Test
    fun `updateCheckBox should update the attendance status of the date`() = runTest {
        val testData = courses.first()
        every { savedStateHandle.get<Int>(CourseDetailsDestination.courseIdArg) } returns testData.course.courseId
        initViewModel()

        
    }

    private fun assertCourseDetails(actual: CourseDetails, expected: Course) {
        assertEquals(expected.toCourseDetails(), actual)
    }

    private fun assertNotes(actual: List<NoteDetails>, expected: List<Note>) {
        assertEquals(expected.map { it.toNoteDetails() }, actual)
    }

    private fun assertDates(actual: List<DateDetails>, expected: List<Date>) {
        assertEquals(expected.map { it.toDateDetails() }, actual)
    }

    private fun assertDeadlines(actual: List<DeadlineDetails>, expected: List<Deadline>) {
        assertEquals(expected.map { it.toDeadlineDetails() }, actual)
    }

    private fun assertImages(actual: List<Image>, expected: List<Image>) {
        assertEquals(expected, actual)
    }

    private fun initViewModel() {
        viewModel = CourseDetailsViewModel(
            courseRepository = courseRepository,
            savedStateHandle = savedStateHandle,
            notesRepository = notesRepository,
            deadlineRepository = deadlineRepository,
            dateRepository = dateRepository,
            imageRepository = imageRepository
        )
    }

    private fun mockRepositories(course: TestCourseData) {
        every { courseRepository.getItemStream(course.course.courseId) } returns flowOf(course.course)
        every { notesRepository.getNotesByCourseId(course.course.courseId) } returns flowOf(
            course.notes
        )
        every { deadlineRepository.getDeadlinesByCourseId(course.course.courseId) } returns flowOf(
            course.deadlines
        )
        every { dateRepository.getDatesByCourseId(course.course.courseId) } returns flowOf(course.dates)
        every { imageRepository.getImageByCourseId(course.course.courseId) } returns flowOf(
            course.images
        )
    }
}