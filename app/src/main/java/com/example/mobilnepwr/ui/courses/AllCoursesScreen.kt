package com.example.mobilnepwr.ui.courses

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilnepwr.R
import com.example.mobilnepwr.data.courses.Course
import com.example.mobilnepwr.ui.AppViewModelProvider
import com.example.mobilnepwr.ui.home.HomeViewModel
import com.example.mobilnepwr.ui.navigation.NavigationDestination

object AllCoursesDestination : NavigationDestination {
    override val route = "all_courses"
    override val titleRes = R.string.app_name
}

@Composable
fun AllCoursesScreen(
    modifier: Modifier = Modifier,
    viewModel: AllCoursesViewModel = viewModel(factory = AppViewModelProvider.Factory),
    contentPadding: PaddingValues
){
    val courseUiState by viewModel.courseUiState.collectAsState()


}

@Composable
fun AllCoursesBody(
    coursesList: List<Course>,
    onCourseClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
){

}