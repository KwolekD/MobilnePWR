package com.example.mobilnepwr.ui.course_deatails.note

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mobilnepwr.R
import com.example.mobilnepwr.ui.course_deatails.NoteDetails
import com.example.mobilnepwr.ui.navigation.ScaffoldViewModel


@Composable
fun Notes(
    notesList: List<NoteDetails>,
    setFabOnClick: (() -> Unit) -> Unit,
    onFabClick: () -> Unit,
    scaffoldViewModel: ScaffoldViewModel,
    onEditClick: (Int) -> Unit,
    onDeleteClick: (NoteDetails) -> Unit,
    clickNote: (Int) -> Unit,
    selectedTab: Int
) {
    LaunchedEffect(selectedTab) {
        setFabOnClick {
            onFabClick()
        }
        scaffoldViewModel.updateState(
            showFab = true,
            iconFab = Icons.Default.Add,
            navigationIcon = Icons.Default.Clear,
            enableGestures = false
        )
    }

    LazyColumn(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize()
    ) {
        item {
            Text(
                text = stringResource(R.string.notes_tab_title),
                style = MaterialTheme.typography.titleLarge,
            )
        }

        itemsIndexed(notesList) { index, note ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable(
                        enabled = note.content.length > 90
                    ) {
                        clickNote(index)

                    }
                    .animateContentSize(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = note.title, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = if (note.isExpanded || note.content.length <= 90) note.content else note.contentShort,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Data: ${note.date}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.align(Alignment.End)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = { onEditClick(note.noteId) }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edytuj notatkę",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.size(8.dp))
                        IconButton(onClick = { onDeleteClick(note) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Usuń notatkę",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}