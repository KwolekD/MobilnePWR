package com.example.mobilnepwr.ui.course_deatails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mobilnepwr.R
import com.example.mobilnepwr.ui.navigation.ScaffoldViewModel
import java.time.format.DateTimeFormatter


@Composable
fun Dates(
    datesList: List<DateDetails>,
    scaffoldViewModel: ScaffoldViewModel,
    viewModel: CourseDetailsViewModel,
    selectedTab: Int
) {
    LaunchedEffect(selectedTab) {
        scaffoldViewModel.updateState(
            showFab = false,
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
                text = stringResource(R.string.dates_tab_title),
                style = MaterialTheme.typography.titleLarge
            )
//            if (datesList.isEmpty())
//                Text(text = "Brak zajęć", style = MaterialTheme.typography.bodyMedium)
        }
        items(datesList) { date ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                )
                {
                    Text(
                        text = date.date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Checkbox(
                        checked = date.attendance,
                        onCheckedChange = { viewModel.updateCheckBox(date) },
                        modifier = Modifier.testTag("checkbox${date.dateId}")
                    )
                }
            }
        }
    }
}