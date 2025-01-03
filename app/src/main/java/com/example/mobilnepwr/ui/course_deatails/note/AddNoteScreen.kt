package com.example.mobilnepwr.ui.course_deatails.note

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilnepwr.ui.AppViewModelProvider
import com.example.mobilnepwr.ui.course_deatails.NoteDetails
import kotlinx.coroutines.launch

@Composable
fun AddNoteScreen(
    navigateBack: () -> Unit,
    viewModel: AddNoteViewModel = viewModel(factory = AppViewModelProvider.Factory),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    AddNoteBody(
        noteDetails = uiState.noteDetails,
        onNoteValueChange = viewModel::updateUiState,
        onSave = {
            coroutineScope.launch {
                viewModel.saveNote()
                navigateBack()
            }
        },
        onCancel = navigateBack,
        isEntryValid = uiState.isEntryValid,
        modifier = modifier.padding(contentPadding)
    )


}

@Composable
fun AddNoteBody(
    noteDetails: NoteDetails,
    onNoteValueChange: (NoteDetails) -> Unit,
    onSave: () -> Unit = {},
    onCancel: () -> Unit = {},
    isEntryValid: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = noteDetails.title,
            onValueChange = { onNoteValueChange(noteDetails.copy(title = it)) },
            label = { Text("Tytuł") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedButton(
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(noteDetails.date.toString() ?: "Wybierz datę")
        }

        TextField(
            value = noteDetails.content,
            onValueChange = { onNoteValueChange(noteDetails.copy(content = it)) },
            label = { Text("Opis") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onSave,
                enabled = isEntryValid,
                modifier = Modifier.weight(1f)
            ) {
                Text("Dodaj")
            }

            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text("Anuluj")
            }
        }

    }
}