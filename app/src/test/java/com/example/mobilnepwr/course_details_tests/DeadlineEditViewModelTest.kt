package com.example.mobilnepwr.course_details_tests

import androidx.lifecycle.SavedStateHandle
import com.example.mobilnepwr.MainCoroutineRule
import com.example.mobilnepwr.data.deadlines.Deadline
import com.example.mobilnepwr.data.deadlines.DeadlineRepository
import com.example.mobilnepwr.ui.course_deatails.DeadlineDetails
import com.example.mobilnepwr.ui.course_deatails.deadline.EditDeadlineViewModel
import com.example.mobilnepwr.ui.navigation.EditDeadlineDestination
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockkClass
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class DeadlineEditViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val deadlineRepository = mockkClass(DeadlineRepository::class)

    private val savedStateHandle = mockkClass(SavedStateHandle::class)

    private lateinit var viewModel: EditDeadlineViewModel

    private val deadlines = listOf(
        DeadlineDetails(
            courseId = 0,
            deadlineId = 1,
            title = "Test title 1",
            description = "Test description 1",
            date = LocalDate.of(2023, 1, 1)
        ),
        DeadlineDetails(
            courseId = 0,
            deadlineId = 2,
            title = "Test title 2",
            description = "Test description 2",
            date = LocalDate.of(2023, 2, 1)
        )
    )

    @Before
    fun setUp() {
        every { deadlineRepository.getItemStream(1) } returns flowOf(
            Deadline(
                deadlineId = 1,
                courseId = 0,
                title = "Test title 1",
                description = "Test description 1",
                date = LocalDate.of(2023, 1, 1)
            )
        )

        every { deadlineRepository.getItemStream(2) } returns flowOf(
            Deadline(
                deadlineId = 2,
                courseId = 0,
                title = "Test title 2",
                description = "Test description 2",
                date = LocalDate.of(2023, 2, 1)
            )
        )
    }

    @Test
    fun `initalized viewModel should update uiState`() {
        every { savedStateHandle.get<Int>(EditDeadlineDestination.deadlineIdArg) } returns 1
        viewModel = EditDeadlineViewModel(savedStateHandle, deadlineRepository)

        assert(viewModel.editDeadlineUiState.value.deadlineDetails == deadlines[0])
        assert(viewModel.editDeadlineUiState.value.isEntryValid)
        assertFalse(viewModel.editDeadlineUiState.value.showDatePicker)

        every { savedStateHandle.get<Int>(EditDeadlineDestination.deadlineIdArg) } returns 2
        viewModel = EditDeadlineViewModel(savedStateHandle, deadlineRepository)

        assert(viewModel.editDeadlineUiState.value.deadlineDetails == deadlines[1])
        assert(viewModel.editDeadlineUiState.value.isEntryValid)
        assertFalse(viewModel.editDeadlineUiState.value.showDatePicker)

    }

    @Test
    fun `updateDeadlineDetails should update deadlineDetails`() {
        every { savedStateHandle.get<Int>(EditDeadlineDestination.deadlineIdArg) } returns 1
        viewModel = EditDeadlineViewModel(savedStateHandle, deadlineRepository)

        viewModel.updateDeadlineDetails(
            viewModel.editDeadlineUiState.value.deadlineDetails.copy(title = "asd")
        )
        assert(viewModel.editDeadlineUiState.value.deadlineDetails == deadlines[0].copy(title = "asd"))

    }

    @Test
    fun `updateDeadlineDetails should update isEntryValid`() {
        every { savedStateHandle.get<Int>(EditDeadlineDestination.deadlineIdArg) } returns 1
        viewModel = EditDeadlineViewModel(savedStateHandle, deadlineRepository)

        val deadlineDetails = DeadlineDetails(
            title = "Test title",
            description = "Test description"
        )
        assert(viewModel.editDeadlineUiState.value.isEntryValid)

        viewModel.updateDeadlineDetails(deadlineDetails)
        assert(viewModel.editDeadlineUiState.value.isEntryValid)

        viewModel.updateDeadlineDetails(deadlineDetails.copy(title = ""))
        assert(!viewModel.editDeadlineUiState.value.isEntryValid)

    }

    @Test
    fun `clickDatePicker should update showDatePicker`() {
        every { savedStateHandle.get<Int>(EditDeadlineDestination.deadlineIdArg) } returns 1
        viewModel = EditDeadlineViewModel(savedStateHandle, deadlineRepository)

        assertFalse(viewModel.editDeadlineUiState.value.showDatePicker)
        viewModel.clickDatePicker()
        assert(viewModel.editDeadlineUiState.value.showDatePicker)

    }

    @Test
    fun `updateDeadline should call deadlineRepository updateDeadline if valid`() = runTest {
        every { savedStateHandle.get<Int>(EditDeadlineDestination.deadlineIdArg) } returns 1
        coEvery { deadlineRepository.updateDeadline(any()) } just runs
        viewModel = EditDeadlineViewModel(savedStateHandle, deadlineRepository)

        viewModel.updateDeadlineDetails(
            viewModel.editDeadlineUiState.value.deadlineDetails.copy(title = "asd")
        )
        viewModel.updateDeadline()

        coVerify { deadlineRepository.updateDeadline(any()) }


    }

    @Test
    fun `updateDeadline should not call deadlineRepository updateDeadline if invalid`() = runTest {
        every { savedStateHandle.get<Int>(EditDeadlineDestination.deadlineIdArg) } returns 1
        coEvery { deadlineRepository.updateDeadline(any()) } just runs
        viewModel = EditDeadlineViewModel(savedStateHandle, deadlineRepository)

        viewModel.updateDeadlineDetails(
            viewModel.editDeadlineUiState.value.deadlineDetails.copy(title = "")
        )
        viewModel.updateDeadline()

        coVerify(exactly = 0) { deadlineRepository.updateDeadline(any()) }

    }
}