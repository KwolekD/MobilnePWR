package com.example.mobilnepwr.ui.course_deatails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilnepwr.R
import com.example.mobilnepwr.ui.AppViewModelProvider
import com.example.mobilnepwr.ui.courses.AllCoursesViewModel
import com.example.mobilnepwr.ui.navigation.NavigationDestination
import kotlinx.coroutines.flow.MutableStateFlow
import javax.security.auth.Subject



@Composable
fun CourseDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: CourseDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    contentPadding: PaddingValues = PaddingValues.Absolute()
){

    val courseUiState = viewModel.courseUiState.collectAsState()
    Column(
        modifier = Modifier
            .padding(contentPadding)
    ) {
        Text(text = courseUiState.value.courseDetails.name)
        Text(text = courseUiState.value.courseDetails.type)
        Text(text = courseUiState.value.courseDetails.hall)
        Text(text = courseUiState.value.courseDetails.building)
        Text(text = courseUiState.value.courseDetails.address)
    }

//    val courseUiState by viewModel.courseUiState.collectAsState()


}


//@Composable
//fun SubjectDetail(subject: Subject) {
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text(text = subject.name) },
//                navigationIcon = {
//                    IconButton(onClick = { /* Wróć do poprzedniego ekranu */ }) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Wróć")
//                    }
//                }
//            )
//        }
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//        ) {
//            // Szczegóły przedmiotu
//            SectionDetails(subject)
//
//            // Zakładki
//            TabLayout(subject)
//        }
//    }
//}

//@Composable
//fun SectionDetails(subject: Subject) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp)
//    ) {
//        DetailRow(Icons.Default.Person, "Prowadzący: ${subject.teacher}")
//        DetailRow(Icons.Default.Schedule, "Godzina: ${subject.time}")
//        DetailRow(Icons.Default.Place, "Adres: ${subject.location}")
//        DetailRow(Icons.Default.Home, "Sala: ${subject.room}")
//    }
//}
