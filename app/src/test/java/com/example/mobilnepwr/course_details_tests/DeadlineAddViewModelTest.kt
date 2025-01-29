package com.example.mobilnepwr.course_details_tests

import androidx.lifecycle.SavedStateHandle
import com.example.mobilnepwr.MainCoroutineRule
import com.example.mobilnepwr.data.deadlines.DeadlineRepository
import com.example.mobilnepwr.ui.course_deatails.DeadlineDetails
import com.example.mobilnepwr.ui.course_deatails.deadline.AddDeadlineViewModel
import com.example.mobilnepwr.ui.navigation.AddDeadlineDestination
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockkClass
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class DeadlineAddViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val deadlineRepository = mockkClass(DeadlineRepository::class)

    private val savedStateHandle = mockkClass(SavedStateHandle::class)

    private lateinit var viewModel: AddDeadlineViewModel

    @Before
    fun setUp() {
        every { savedStateHandle.get<Int>(AddDeadlineDestination.courseIdArg) } returns 0
        viewModel = AddDeadlineViewModel(savedStateHandle, deadlineRepository)
    }

    @Test
    fun `updateDeadlineDetails should update deadlineDetails`() {
        val deadlineDetails = DeadlineDetails(
            title = "Test title",
            description = "Test description",
            date = LocalDate.of(2023, 1, 1)
        )
        viewModel.updateDeadlineDetails(
            deadlineDetails
        )
        assertEquals(viewModel.uiState.value.deadlineDetails, deadlineDetails)
    }

    @Test
    fun `updateDeadlineDetails should update isEntryValid`() {
        val deadlineDetails = DeadlineDetails(
            title = "Test title",
            description = "Test description",
            date = LocalDate.of(2023, 1, 1)
        )

        assertFalse(viewModel.uiState.value.isEntryValid)

        viewModel.updateDeadlineDetails(deadlineDetails)
        assertTrue(viewModel.uiState.value.isEntryValid)

        viewModel.updateDeadlineDetails(deadlineDetails.copy(title = ""))
        assertFalse(viewModel.uiState.value.isEntryValid)
    }

    @Test
    fun `clickDatePicker should toggle showDatePicker`() {
        viewModel.clickDatePicker()
        assertEquals(viewModel.uiState.value.showDatePicker, true)
        viewModel.clickDatePicker()
        assertEquals(viewModel.uiState.value.showDatePicker, false)
    }

    @Test
    fun `saveDeadline should call deadlineRepository insertDeadline if valid`() = runTest {
        coEvery { deadlineRepository.insertDeadline(any()) } just Runs
        val deadlineToAdd = DeadlineDetails(
            title = "Test title",
            description = "Test description",
            date = LocalDate.of(2023, 1, 1)
        )

        viewModel.updateDeadlineDetails(deadlineToAdd)
        viewModel.saveDeadline()

        coVerify { deadlineRepository.insertDeadline(any()) }


    }

    @Test
    fun `saveDeadline should not call deadlineRepository insertDeadline if invalid`() = runTest {
        coEvery { deadlineRepository.insertDeadline(any()) } just Runs
        val deadlineToAdd = DeadlineDetails(
            title = "",
            description = "Test description",
            date = LocalDate.of(2023, 1, 1)
        )

        viewModel.updateDeadlineDetails(deadlineToAdd)
        viewModel.saveDeadline()

        coVerify(exactly = 0) { deadlineRepository.insertDeadline(any()) }

    }

}