package com.example.mobilnepwr.ui.course_deatails.deadline

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilnepwr.ui.AppViewModelProvider.Factory
import com.example.mobilnepwr.ui.components.DatePickerModalInput
import com.example.mobilnepwr.ui.course_deatails.DeadlineDetails
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDeadlineScreen(
    contentPadding: PaddingValues,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditDeadlineViewModel = viewModel(factory = Factory),
) {
    val uiState by viewModel.editDeadlineUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    EditDeadlineBody(
        deadlineDetails = uiState.deadlineDetails,
        onDeadlineValueChange = viewModel::updateDeadlineDetails,
        onDatePickerClick = viewModel::clickDatePicker,
        showDatePicker = uiState.showDatePicker,
        onSave = {
            coroutineScope.launch {
                viewModel.updateDeadline()
                navigateBack()
            }
        },
        onCancel = navigateBack,
        isEntryValid = uiState.isEntryValid,
        modifier = modifier.padding(contentPadding)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDeadlineBody(
    deadlineDetails: DeadlineDetails,
    onDeadlineValueChange: (DeadlineDetails) -> Unit,
    showDatePicker: Boolean = false,
    onDatePickerClick: () -> Unit = {},
    onSave: () -> Unit = {},
    onCancel: () -> Unit = {},
    isEntryValid: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Edytuj termin",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        OutlinedTextField(
            value = deadlineDetails.title,
            onValueChange = { onDeadlineValueChange(deadlineDetails.copy(title = it)) },
            label = { Text("Tytuł") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = deadlineDetails.date.toString(),
            onValueChange = { },
            label = { Text("Data") },
            placeholder = { Text("DD/MM/YYYY") },
            trailingIcon = {
                Icon(Icons.Default.DateRange, contentDescription = "Select date")
            },
            modifier = modifier
                .fillMaxWidth()
                .pointerInput(deadlineDetails.date) {
                    awaitEachGesture {
                        // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                        // in the Initial pass to observe events before the text field consumes them
                        // in the Main pass.
                        awaitFirstDown(pass = PointerEventPass.Initial)
                        val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                        if (upEvent != null) {
                            onDatePickerClick()
                        }
                    }
                }
        )

        if (showDatePicker) {
            DatePickerModalInput(
                onDateSelected = {
                    if (it == null) {
                    } else
                        onDeadlineValueChange(deadlineDetails.copy(date = LocalDate.ofEpochDay(it)))
                },
                onDismiss = onDatePickerClick
            )
        }

        OutlinedTextField(
            value = deadlineDetails.description,
            onValueChange = { onDeadlineValueChange(deadlineDetails.copy(description = it)) },
            label = { Text("Opis") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 5
        )


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = onSave,
                enabled = isEntryValid
            ) {
                Text("Zapisz")
            }

            Button(onClick = onCancel) {
                Text("Anuluj")
            }
        }
    }
}
