package com.example.mobilnepwr.ui.course_deatails.deadline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilnepwr.ui.AppViewModelProvider.Factory
import com.example.mobilnepwr.ui.course_deatails.DeadlineDetails
import kotlinx.coroutines.launch

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
        modifier = modifier.padding(contentPadding)
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
    modifier: Modifier = Modifier
) {
    val datePickerState = rememberDatePickerState()
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Dodaj Nowy Termin",
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
            label = { Text("DOB") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { onDatePickerClick() }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        )

        if (showDatePicker) {
            Popup(
                onDismissRequest = {
                    onDatePickerClick()
                },
                alignment = Alignment.BottomEnd
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 64.dp)
                        .shadow(elevation = 4.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false
                    )
                }
            }
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
                Text("Dodaj")
            }

            Button(onClick = onCancel) {
                Text("Anuluj")
            }
        }
    }
}
