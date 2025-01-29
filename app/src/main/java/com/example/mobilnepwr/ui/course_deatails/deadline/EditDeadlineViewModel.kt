package com.example.mobilnepwr.ui.course_deatails.deadline

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilnepwr.data.deadlines.Deadline
import com.example.mobilnepwr.data.deadlines.DeadlineRepository
import com.example.mobilnepwr.ui.course_deatails.DeadlineDetails
import com.example.mobilnepwr.ui.course_deatails.toDeadlineDetails
import com.example.mobilnepwr.ui.navigation.EditDeadlineDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditDeadlineViewModel(
    savedStateHandle: SavedStateHandle,
    private val deadlineRepository: DeadlineRepository
) : ViewModel() {

    private val deadlineId: Int =
        checkNotNull(savedStateHandle[EditDeadlineDestination.deadlineIdArg])

    private val _editDeadlineUiState = MutableStateFlow(AddDeadlineUiState())
    val editDeadlineUiState: StateFlow<AddDeadlineUiState> = _editDeadlineUiState.asStateFlow()


    init {
        viewModelScope.launch {
            _editDeadlineUiState.value = AddDeadlineUiState(
                deadlineDetails = deadlineRepository.getItemStream(deadlineId)
                    .filterNotNull()
                    .first()
                    .toDeadlineDetails(),
                showDatePicker = false,
                isEntryValid = true
            )

        }
    }

    fun updateDeadlineDetails(deadlineDetails: DeadlineDetails) {
        _editDeadlineUiState.value = _editDeadlineUiState.value.copy(
            deadlineDetails = deadlineDetails,
            isEntryValid = validateInput(deadlineDetails)
        )
    }

    fun clickDatePicker() {
        _editDeadlineUiState.update {
            it.copy(
                showDatePicker = !it.showDatePicker
            )
        }
    }

    suspend fun updateDeadline() {
        if (_editDeadlineUiState.value.isEntryValid) {
            deadlineRepository.updateDeadline(
                _editDeadlineUiState.value.deadlineDetails.toDeadline()
            )
        }


    }

    private fun validateInput(deadlineDetails: DeadlineDetails): Boolean {
        return with(deadlineDetails) {
            title.isNotBlank() && description.isNotBlank()
        }
    }
}

fun DeadlineDetails.toDeadline(): Deadline = Deadline(
    deadlineId = deadlineId,
    courseId = courseId,
    title = title,
    date = date,
    description = description
)