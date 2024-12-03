package com.example.mobilnepwr.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilnepwr.ui.AppViewModelProvider
import com.example.mobilnepwr.data.courses.Course
import kotlinx.coroutines.flow.StateFlow



@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    contentPadding: PaddingValues,
    navigateToCourseDetails: (Int) -> Unit
){
    val homeUiState by viewModel.homeUiState.collectAsState()
    DayList(
        daysList = homeUiState.weekDays,
        modifier = modifier.fillMaxSize(),
        onCourseClick = navigateToCourseDetails,
        onDayClick = {index -> viewModel.onDayClick(index)},
        contentPadding = contentPadding)
}


@Composable
fun DayList(
    daysList: List<WeekDay>,
    onCourseClick: (Int) -> Unit,
    onDayClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
){
    LazyColumn(modifier.padding(contentPadding)) {
        itemsIndexed(daysList){index,item ->
            ListItem(
                headlineContent = { Text(
                    text = item.name,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center)},
                modifier = modifier.then(Modifier
                    .clickable { onDayClick(index) }
                    .padding(20.dp)
                    .padding(vertical = 35.dp)
                ))
                CourseList(
                    onCourseClick = onCourseClick,
                    courseList = item.courses,
                    show = item.isExpanded,
                    modifier = Modifier
                        .fillMaxWidth()
                )

        }
    }
}


@Composable
fun CourseList(
    onCourseClick: (Int) -> Unit,
    courseList: List<Course>,
    show: Boolean,
    modifier: Modifier = Modifier
){
    Column (modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally){
        if (show) {
            if (courseList.isEmpty()){
                Text("nic tu nie ma")
            }
            else{
                courseList.forEach { course ->

                    ListItem(
                        headlineContent = { Text(text = course.name) },
                        leadingContent = { Text(text = course.type) },
                        modifier = Modifier
                            .clickable { onCourseClick(course.courseId) })

                }
            }

        }

    }
}

