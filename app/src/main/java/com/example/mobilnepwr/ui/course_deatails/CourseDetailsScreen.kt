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
import androidx.compose.material3.Card
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilnepwr.R
import com.example.mobilnepwr.data.courses.Course
import com.example.mobilnepwr.ui.AppViewModelProvider
import com.example.mobilnepwr.ui.courses.AllCoursesViewModel
import com.example.mobilnepwr.ui.navigation.NavigationDestination
import kotlinx.coroutines.flow.MutableStateFlow
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
//            1 -> Notes()
        }
    }

}

@Composable
fun Details(
    courseDetails: CourseDetails
){
    Text(text = courseDetails.name)
    Text(text = courseDetails.type)
    Text(text = courseDetails.hall)
    Text(text = courseDetails.building)
    Text(text = courseDetails.address)

}



