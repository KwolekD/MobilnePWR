package com.example.mobilnepwr.ui.course_deatails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilnepwr.R
import com.example.mobilnepwr.data.courses.Course
import com.example.mobilnepwr.data.notes.OfflineNoteRepository
import com.example.mobilnepwr.ui.AppViewModelProvider
import com.example.mobilnepwr.ui.courses.AllCoursesViewModel
import com.example.mobilnepwr.ui.navigation.NavigationDestination
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Date
import javax.security.auth.Subject


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: CourseDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    contentPadding: PaddingValues = PaddingValues.Absolute()
){

    val courseUiState by viewModel.courseUiState.collectAsState()

    Column (modifier= Modifier
        .fillMaxSize()
        .padding(contentPadding)){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${courseUiState.courseDetails.type} - ${courseUiState.courseDetails.name}",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
            }
        }


        PrimaryScrollableTabRow(
            edgePadding = 0.dp,
            tabs = {
                Tab(selected = courseUiState.selectedTab == 0, onClick = {viewModel.selectTab(0)}, text = { Text("Szczegóły")})
                Tab(selected = courseUiState.selectedTab == 1, onClick = {viewModel.selectTab(1)}, text = { Text("Notatki")})
                Tab(selected = courseUiState.selectedTab == 2, onClick = {viewModel.selectTab(2)}, text = { Text("Terminy")})
                Tab(selected = courseUiState.selectedTab == 3, onClick = {viewModel.selectTab(3)}, text = { Text("Obecność")})
                Tab(selected = courseUiState.selectedTab == 4, onClick = {viewModel.selectTab(4)}, text = { Text("Zdjęcia")})
                   },
            selectedTabIndex = courseUiState.selectedTab

        )

        when (courseUiState.selectedTab) {
            0 -> Details(courseUiState.courseDetails)
            1 -> Notes(
                notesList = courseUiState.notesList,
                onAddNote = { title, content -> viewModel.addNote(title, content) }
            )
        }
    }

}

@Composable
fun Details(
    courseDetails: CourseDetailsViewModel.CourseDetails
){
    Text(text = courseDetails.name)
    Text(text = courseDetails.type)
    Text(text = courseDetails.hall)
    Text(text = courseDetails.building)
    Text(text = courseDetails.address)

}
@Composable
fun Notes(
    notesList: List<CourseDetailsViewModel.NotesDetails>, // Lista notatek
    onAddNote: (String, String) -> Unit // Funkcja dodająca notatkę
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        // Formularz do wpisywania tytułu i treści notatki
        Text(text = "Dodaj nową notatkę", style = MaterialTheme.typography.titleMedium)

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Tytuł") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Treść") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        Button(
            onClick = {
                if (title.isNotBlank() && content.isNotBlank()) {
                    onAddNote(title, content) // Dodanie notatki
                    title = "" // Wyczyszczenie formularza
                    content = ""
                }
            },
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 8.dp)
        ) {
            Text("Zapisz")
        }

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



