package com.example.mobilnepwr.ui.course_deatails.deadline

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.mobilnepwr.data.deadlines.Deadline
import com.example.mobilnepwr.data.deadlines.DeadlineRepository
import com.example.mobilnepwr.ui.course_deatails.DeadlineDetails
import com.example.mobilnepwr.ui.navigation.AddDeadlineDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddDeadlineViewModel(
    savedStateHandle: SavedStateHandle,
    private val deadlineRepository: DeadlineRepository,
) : ViewModel() {
    val courseId: Int = checkNotNull(savedStateHandle[AddDeadlineDestination.courseIdArg])

    private val _uiState = MutableStateFlow(AddDeadlineUiState())
    val uiState: StateFlow<AddDeadlineUiState> = _uiState.asStateFlow()

    fun updateDeadlineDetails(deadlineDetails: DeadlineDetails) {
        _uiState.value =
            AddDeadlineUiState(deadlineDetails = deadlineDetails, isEntryValid = validateInput())
    }

    fun clickDatePicker() {
        _uiState.update {
            it.copy(
                showDatePicker = !it.showDatePicker
            )
        }
    }

    fun validateInput(): Boolean {
        return with(_uiState.value.deadlineDetails) {
            title.isNotBlank() && date != null && description.isNotBlank()
        }
    }

    suspend fun saveDeadline() {
        if (validateInput()) {
            deadlineRepository.insertDeadline(_uiState.value.deadlineDetails.toDeadline(courseId))
        }
    }

}

data class AddDeadlineUiState(
    val deadlineDetails: DeadlineDetails = DeadlineDetails(),
    val showDatePicker: Boolean = false,
    val isEntryValid: Boolean = false
)

fun DeadlineDetails.toDeadline(courseId: Int): Deadline = Deadline(
    courseId = courseId,
    title = title,
    description = description,
    date = date,
    deadlineId = 0
)