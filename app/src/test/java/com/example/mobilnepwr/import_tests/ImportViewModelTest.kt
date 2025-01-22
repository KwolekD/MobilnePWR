package com.example.mobilnepwr.import_tests

import com.example.mobilnepwr.MainCoroutineRule
import com.example.mobilnepwr.data.courses.CourseRepository
import com.example.mobilnepwr.data.dates.DateRepository
import com.example.mobilnepwr.ui.import.ImportUiState
import com.example.mobilnepwr.ui.import.ImportViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkClass
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

class ImportViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val courseRepository = mockkClass(CourseRepository::class)
    private val dateRepository = mockkClass(DateRepository::class)

    private lateinit var viewModel: ImportViewModel

    @Before
    fun setUp() {
        every { courseRepository.getAllItemsStream() } returns flowOf(emptyList())
        coEvery { courseRepository.importCoursesFromIcal(any()) } just Runs
        viewModel = ImportViewModel(dateRepository, courseRepository)
    }

    @Test
    fun `updateUiState should update importUiState correctly`() {
        viewModel.updateUiState(
            importLink = "http://example.com", showInfo = true, showConfirmationDialog = true,
        )

        val state = viewModel.importUiState
        assertEquals("http://example.com", state.importLink)
        assertTrue(state.showInfo)
        assertFalse(state.showError)
        assertTrue(state.showConfirmationDialog)

        viewModel.updateUiState(
            ImportUiState(importLink = "link")
        )
        val state2 = viewModel.importUiState
        assertEquals("link", state2.importLink)
        assertFalse(state2.showInfo)
        assertFalse(state2.showError)
        assertFalse(state2.showConfirmationDialog)
    }

    @Test
    fun `clickFAB toggles showInfo state`() {
        // Act
        viewModel.clickFAB()

        // Assert
        assertTrue(viewModel.importUiState.showInfo)

        // Act again
        viewModel.clickFAB()

        // Assert
        assertFalse(viewModel.importUiState.showInfo)
    }

    @Test
    fun `importData updates repositories and handles success`() = runTest {
        // Arrange
        val icalData = "BEGIN:VCALENDAR\nEND:VCALENDAR"
        val tempFile = File.createTempFile("calendar", ".ics")
        tempFile.writeText(icalData)

        mockkStatic(File::class)
        every { File.createTempFile("calendar", ".ics") } returns tempFile

        coEvery { courseRepository.importCoursesFromIcal(any()) } just Runs
        coEvery { dateRepository.importDatesFromIcal(any(), any()) } just Runs
        coEvery { courseRepository.getAllItemsStream() } returns flowOf(emptyList())

        val mockResponse = mockk<okhttp3.Response> {
            every { isSuccessful } returns true
            every { body } returns mockk {
                every { bytes() } returns icalData.toByteArray()
            }
        }

        mockkConstructor(OkHttpClient::class)
        every { anyConstructed<OkHttpClient>().newCall(any()).execute() } returns mockResponse

        viewModel.updateUiState(importLink = "http://example.com")

        // Act
        viewModel.importData()

        // Assert
        coVerify { courseRepository.importCoursesFromIcal(any()) }
        coVerify { dateRepository.importDatesFromIcal(any(), any()) }
        assertFalse(viewModel.importUiState.showError)

        tempFile.delete()
    }

    @Test
    fun `importData sets showError to true on error`() = runTest {
        coEvery {
            anyConstructed<OkHttpClient>().newCall(any()).execute()
        } throws Exception("Network error")

        viewModel.updateUiState(importLink = "http://example.com")

        viewModel.importData()

        assertTrue(viewModel.importUiState.showError)
    }

    @Test
    fun `isDatabaseNotEmpty returns true if repository is not empty`() = runTest {
        coEvery { courseRepository.getAllItemsStream() } returns flowOf(listOf(mockk()))

        val result = viewModel.isDatabaseNotEmpty()

        assertTrue(result)
    }

    @Test
    fun `isDatabaseNotEmpty returns false if repository is empty`() = runTest {
        coEvery { courseRepository.getAllItemsStream() } returns flowOf(emptyList())

        val result = viewModel.isDatabaseNotEmpty()

        assertFalse(result)
    }
}