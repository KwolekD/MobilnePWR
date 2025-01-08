package com.example.mobilnepwr.ui.course_deatails.deadline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobilnepwr.ui.course_deatails.DeadlineDetails
import com.example.mobilnepwr.ui.navigation.ScaffoldViewModel
import java.time.format.DateTimeFormatter

@Composable
fun Deadlines(
    deadlinesList: List<DeadlineDetails>,
    setFabOnClick: (() -> Unit) -> Unit,
    onFabClick: () -> Unit,
    onEditClick: (Int) -> Unit,
    onDeleteClick: (DeadlineDetails) -> Unit,
    scaffoldViewModel: ScaffoldViewModel
) {
    LaunchedEffect(Unit) {
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
            Text(text = "Twoje terminy:", style = MaterialTheme.typography.titleLarge)
//            if (deadlinesList.isEmpty()) {
//                Text(
//                    text = "Dodaj terminy",
//                    style = MaterialTheme.typography.titleMedium,
//                    color = MaterialTheme.colorScheme.error
//                )
//            }
        }
        items(deadlinesList) { deadline ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            )
            {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = deadline.title, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = deadline.date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                        style = MaterialTheme.typography.titleSmall,
                    )
                    Text(text = deadline.description, style = MaterialTheme.typography.bodyLarge)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = { onEditClick(deadline.deadlineId) }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edytuj termin",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.size(8.dp))
                        IconButton(onClick = { onDeleteClick(deadline) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Usuń termin",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}