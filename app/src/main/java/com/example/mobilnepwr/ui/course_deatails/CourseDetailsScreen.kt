package com.example.mobilnepwr.ui.course_deatails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilnepwr.ui.AppViewModelProvider
import com.example.mobilnepwr.ui.navigation.ScaffoldViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: CourseDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    setFabOnClick: (() -> Unit) -> Unit,
    navigateToAddDeadline: (Int) -> Unit,
    navigateToAddNote: (Int) -> Unit,
    contentPadding: PaddingValues,
    scaffoldViewModel: ScaffoldViewModel
) {
    val courseUiState by viewModel.courseUiState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${courseUiState.courseDetails.type} - ${courseUiState.courseDetails.name}",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val tabs = listOf("Szczegóły", "Notatki", "Terminy", "Obecność", "Zdjęcia")

        PrimaryScrollableTabRow(
            edgePadding = 0.dp,
            tabs = {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = courseUiState.selectedTab == index,
                        onClick = { viewModel.selectTab(index) },
                        text = { Text(title) })
                }
            },
            selectedTabIndex = courseUiState.selectedTab

        )

        when (courseUiState.selectedTab) {
            0 -> Details(
                courseUiState.courseDetails,
                scaffoldViewModel
            )

            1 -> Notes(
                notesList = courseUiState.notesList,
                setFabOnClick = setFabOnClick,
                onFabClick = { navigateToAddNote(viewModel.courseId) },
                scaffoldViewModel = scaffoldViewModel
            )

            2 -> Deadlines(
                deadlinesList = courseUiState.deadlinesList,
                setFabOnClick = setFabOnClick,
                onFabClick = { navigateToAddDeadline(viewModel.courseId) },
                scaffoldViewModel = scaffoldViewModel
            )

            3 -> Dates(
                datesList = courseUiState.datesList,
                scaffoldViewModel = scaffoldViewModel
            )
        }
    }

}

@Composable
fun Deadlines(
    deadlinesList: List<DeadlineDetails>,
    setFabOnClick: (() -> Unit) -> Unit,
    onFabClick: () -> Unit,
    scaffoldViewModel: ScaffoldViewModel
) {
    LaunchedEffect(Unit) {
        setFabOnClick {
            onFabClick()
        }
        scaffoldViewModel.updateState(
            showFab = true,
            iconFab = Icons.Default.Add,
            navigationIcon = Icons.Default.Clear
        )
    }
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Twoje terminy:", style = MaterialTheme.typography.titleLarge)
        deadlinesList.forEach { deadline ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            )
            {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = deadline.title, style = MaterialTheme.typography.titleSmall)
                    Text(text = deadline.description, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = "Data: ${deadline.date}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        }
    }
}

@Composable
fun Dates(
    datesList: List<DateDetails>,
    scaffoldViewModel: ScaffoldViewModel
) {
    LaunchedEffect(Unit) {
        scaffoldViewModel.updateState(
            showFab = false,
            navigationIcon = Icons.Default.Clear
        )
    }
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Twoje obecności:", style = MaterialTheme.typography.titleLarge)
        if (datesList.isEmpty())
            Text(text = "Brak obecności", style = MaterialTheme.typography.bodyMedium)
        datesList.forEach { date ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(modifier = Modifier.padding(8.dp))
                {
                    Text(text = date.date.toString(), style = MaterialTheme.typography.titleSmall)
                    Checkbox(
                        checked = date.attendance,
                        onCheckedChange = {},

                        )
                }
            }


        }
    }
}

@Composable
fun Details(

    courseDetails: CourseDetails,
    scaffoldViewModel: ScaffoldViewModel,
    itemModifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
) {
    LaunchedEffect(Unit) {
        scaffoldViewModel.updateState(
            showFab = false,
            navigationIcon = Icons.Default.Clear
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val items = listOf(
            Triple(Icons.Rounded.Place, courseDetails.name, "name"),
            Triple(
                Icons.Default.Refresh,
                when (courseDetails.type) {
                    "L" -> "Laboratorium"
                    "P" -> "Projekt"
                    "C" -> "Ćwiczenia"
                    "W" -> "Wykład"
                    "S" -> "Seminarium"
                    else -> ""

                }, "type"
            ),
            Triple(Icons.Rounded.Home, courseDetails.hall, "hall"),
            Triple(Icons.Default.Home, courseDetails.building, "building"),
            Triple(Icons.Default.Place, courseDetails.address, "address")
        )

        items.forEach { (icon, text, description) ->
            ListItem(
                leadingContent = {
                    Icon(
                        imageVector = icon,
                        contentDescription = description
                    )
                },
                headlineContent = {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                modifier = itemModifier
            )
        }
    }
}


@Composable
fun Notes(
    notesList: List<NoteDetails>,
    setFabOnClick: (() -> Unit) -> Unit,
    onFabClick: () -> Unit,
    scaffoldViewModel: ScaffoldViewModel
) {
    LaunchedEffect(Unit) {
        setFabOnClick {
            onFabClick()
        }
        scaffoldViewModel.updateState(
            showFab = true,
            iconFab = Icons.Default.Add,
            navigationIcon = Icons.Default.Clear
        )
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // Formularz do wpisywania tytułu i treści notatki
//        Text(text = "Dodaj nową notatkę", style = MaterialTheme.typography.titleMedium)
//
//        OutlinedTextField(
//            value = title,
//            onValueChange = { title = it },
//            label = { Text("Tytuł") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        OutlinedTextField(
//            value = content,
//            onValueChange = { content = it },
//            label = { Text("Treść") },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 8.dp)
//        )
//
//        Button(
//            onClick = {
//                if (title.isNotBlank() && content.isNotBlank()) {
//                    onAddNote(title, content) // Dodanie notatki
//                    title = "" // Wyczyszczenie formularza
//                    content = ""
//                }
//            },
//            modifier = Modifier
//                .align(Alignment.End)
//                .padding(top = 8.dp)
//        ) {
//            Text("Zapisz")
//        }

        // Wyświetlanie listy notatek
        Text(
            text = "Twoje notatki:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 16.dp)
        )

        notesList.forEach { note ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = note.title, style = MaterialTheme.typography.titleSmall)
                    Text(text = note.content, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = "Data: ${note.date}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        }
    }
}


