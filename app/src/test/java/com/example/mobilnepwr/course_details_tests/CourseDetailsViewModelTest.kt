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
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockkClass
import io.mockk.runs
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
        coEvery { notesRepository.deleteNote(any()) } just runs
        coEvery { deadlineRepository.deleteDeadline(any()) } just runs
        coEvery { imageRepository.deleteItem(any()) } just runs
        coEvery { dateRepository.updateDate(any()) } just runs
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
        assertEquals(0, viewModel.courseUiState.value.selectedTab)
        viewModel.selectTab(1)
        assertEquals(viewModel.courseUiState.value.selectedTab, 1)
        assertEquals(viewModel.courseUiState.value.prevSelectedTab, 0)

        viewModel.selectTab(3)
        assertEquals(viewModel.courseUiState.value.selectedTab, 3)
        assertEquals(viewModel.courseUiState.value.prevSelectedTab, 1)
    }

    @Test
    fun `deleteNote should call delete in repository`() = runTest {
        val testData = courses.first()
        every { savedStateHandle.get<Int>(CourseDetailsDestination.courseIdArg) } returns testData.course.courseId
        initViewModel()

        val noteToDelete = testData.notes.first()
        viewModel.deleteNote(noteToDelete.toNoteDetails())
        coVerify {
            notesRepository.deleteNote(noteToDelete)
        }
    }

    @Test
    fun `deleteDeadline should call delete in repository`() = runTest {
        val testData = courses.first()
        every { savedStateHandle.get<Int>(CourseDetailsDestination.courseIdArg) } returns testData.course.courseId
        initViewModel()

        val deadlineToDelete = testData.deadlines[1]
        viewModel.deleteDeadline(deadlineToDelete.toDeadlineDetails())
        coVerify {
            deadlineRepository.deleteDeadline(deadlineToDelete)
        }
    }

    @Test
    fun `deletePhoto should call delete in repository and delete photo`() = runTest {
        val testData = courses.first()
        val imageToDelete = testData.images.first()
        every { savedStateHandle.get<Int>(CourseDetailsDestination.courseIdArg) } returns testData.course.courseId

        initViewModel()
        viewModel.deletePhoto(imageToDelete)
        coVerify {
            imageRepository.deleteItem(imageToDelete)
        }
    }

    @Test
    fun `updateCheckBox should update the attendance status of the date`() = runTest {
        val testData = courses.first()
        every { savedStateHandle.get<Int>(CourseDetailsDestination.courseIdArg) } returns testData.course.courseId
        initViewModel()

        val dateToUpdate = testData.dates.first()
        viewModel.updateCheckBox(dateToUpdate.toDateDetails())
        coVerify {
            dateRepository.updateDate(dateToUpdate.copy(attendance = !dateToUpdate.attendance))
        }
    }

//    @Test
//    fun `savePhoto calls repository insertItem when image is saved successfully`() = runTest {
//        val testData = courses[1]
//        val filePath = "mock/path/photo.jpg"
//        var slot = slot<Image>()
//
//        val context = mockk<Context>()
//        val uri = mockk<Uri>()
//        val contentResolver = mockk<ContentResolver>()
//        val inputStream = ByteArrayInputStream(ByteArray(0)) // Mock pustego pliku
//        val file = mockk<File>()
//
//        every { file.parent } returns "mock/filesDir"
//        every { context.contentResolver } returns contentResolver
//        every { contentResolver.openInputStream(uri) } returns inputStream
//        every { context.filesDir } returns file
//        mockkStatic(File::class) // Mockowanie tworzenia plików
//        every { File(file, any()).absolutePath } returns filePath
//
//        every { savedStateHandle.get<Int>(CourseDetailsDestination.courseIdArg) } returns testData.course.courseId
//        coEvery { imageRepository.insertItem(capture(slot)) } just runs
//        initViewModel()
//        every { viewModel.saveImageToAppStorage(any(), any()) } returns filePath
//        // Act
//        viewModel.savePhoto(context, uri)
//
//        coVerify { imageRepository.insertItem(any()) }
//
//        val capturedImage = slot.captured
//        assertEquals(viewModel.courseId, capturedImage.courseId)
//        assertEquals(filePath, capturedImage.filePath)
//        assertEquals(0, capturedImage.imageId) // Sprawdza domyślny identyfikator
//        // Assert
////        coVerify {
////            imageRepository.insertItem(
////                Image(
////                    courseId = viewModel.courseId,
////                    filePath = filePath,
////                    imageId = 0
////                )
////            )
////        }
//
//    }


//    @Test
//    fun `saveImageToAppStorage saves image and returns file path`() {
//        // Arrange
//        val context = ApplicationProvider.getApplicationContext<Context>()
//        val testUri = Uri.parse("android.resource://your.package.name/drawable/test_image")
//
//        // Act
//        val path = viewModel.saveImageToAppStorage(context, testUri)
//
//        // Assert
//        assertNotNull(path)
//        assertTrue(File(path!!).exists())
//    }

    @Test
    fun `clickNote should toggle the isExpanded state of the note`() = runTest {
        val testData = courses[1]
        every { savedStateHandle.get<Int>(CourseDetailsDestination.courseIdArg) } returns testData.course.courseId
        initViewModel()
        viewModel.clickNote(0)
        assertEquals(viewModel.courseUiState.value.notesList.first().isExpanded, true)
        assertEquals(viewModel.courseUiState.value.notesList[1].isExpanded, false)


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