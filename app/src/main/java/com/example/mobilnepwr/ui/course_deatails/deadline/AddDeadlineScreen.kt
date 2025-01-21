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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilnepwr.R
import com.example.mobilnepwr.ui.AppViewModelProvider.Factory
import com.example.mobilnepwr.ui.components.DatePickerModalInput
import com.example.mobilnepwr.ui.course_deatails.DeadlineDetails
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDeadlineScreen(
    viewModel: AddDeadlineViewModel = viewModel(factory = Factory),
    navigateBack: () -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    AddDeadlineBody(
        deadlineDetails = uiState.deadlineDetails,
        onDeadlineValueChange = viewModel::updateDeadlineDetails,
        onDatePickerClick = viewModel::clickDatePicker,
        showDatePicker = uiState.showDatePicker,
        onSave = {
            coroutineScope.launch {
                viewModel.saveDeadline()
                navigateBack()
            }
        },
        onCancel = {
            navigateBack()
        },
        isEntryValid = uiState.isEntryValid,
        contentPadding = contentPadding
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDeadlineBody(
    deadlineDetails: DeadlineDetails,
    onDeadlineValueChange: (DeadlineDetails) -> Unit,
    showDatePicker: Boolean = false,
    onDatePickerClick: () -> Unit = {},
    onSave: () -> Unit = {},
    onCancel: () -> Unit = {},
    isEntryValid: Boolean = false,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        Text(
            text = stringResource(R.string.add_deadline_title),
            style = MaterialTheme.typography.headlineMedium,
            modifier = modifier,
            textAlign = TextAlign.Center
        )

        OutlinedTextField(
            value = deadlineDetails.title,
            singleLine = true,
            onValueChange = { onDeadlineValueChange(deadlineDetails.copy(title = it)) },
            label = { Text(stringResource(R.string.title_label)) },
            modifier = modifier
        )

        OutlinedTextField(
            value = deadlineDetails.date.format(DateTimeFormatter.ofPattern("DD-MM-yyyy")),
            onValueChange = { },
            label = { Text(stringResource(R.string.date_label)) },
            placeholder = { Text("DD/MM/YYYY") },
            trailingIcon = {
                Icon(Icons.Default.DateRange, contentDescription = "Select date")
            },
            modifier = modifier
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
                    if (it != null)
                        onDeadlineValueChange(deadlineDetails.copy(date = LocalDate.ofEpochDay(it / (24 * 60 * 60 * 1000))))
                },
                onDismiss = onDatePickerClick,
                selectedDate = deadlineDetails.date.atStartOfDay().toInstant(ZoneOffset.UTC)
                    .toEpochMilli()
            )
        }

        OutlinedTextField(
            value = deadlineDetails.description,
            onValueChange = { onDeadlineValueChange(deadlineDetails.copy(description = it)) },
            label = { Text(stringResource(R.string.description_label)) },
            modifier = modifier,
            maxLines = 5,
        )


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = onSave,
                enabled = isEntryValid
            ) {
                Text(stringResource(R.string.add_button_label))
            }

            Button(onClick = onCancel) {
                Text(stringResource(R.string.cancel_button_label))
            }
        }
    }
}
