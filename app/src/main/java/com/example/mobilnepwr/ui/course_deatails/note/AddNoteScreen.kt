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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilnepwr.R
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
        contentPadding = contentPadding
    )


}

@Composable
fun AddNoteBody(
    noteDetails: NoteDetails,
    onNoteValueChange: (NoteDetails) -> Unit,
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
            .padding(contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.add_note_title),
            style = MaterialTheme.typography.headlineMedium,
            modifier = modifier,
            textAlign = TextAlign.Center
        )
        TextField(
            value = noteDetails.title,
            onValueChange = { onNoteValueChange(noteDetails.copy(title = it)) },
            label = { Text(stringResource(R.string.title_label)) },
            modifier = modifier
        )


        TextField(
            value = noteDetails.content,
            onValueChange = { onNoteValueChange(noteDetails.copy(content = it)) },
            label = { Text(stringResource(R.string.description_label)) },
            modifier = modifier
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
        ) {
            Button(
                onClick = onSave,
                enabled = isEntryValid,
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.add_button_label))
            }

            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.cancel_button_label))
            }
        }

    }
}