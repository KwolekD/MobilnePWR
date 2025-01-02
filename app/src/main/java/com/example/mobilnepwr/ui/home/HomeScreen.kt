package com.example.mobilnepwr.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilnepwr.ui.AppViewModelProvider
import com.example.mobilnepwr.data.courses.Course
import kotlin.math.exp


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
        onDayClick = viewModel::onDayClick,
        contentPadding = contentPadding
    )

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
                    .padding(vertical = 20.dp)
                ))
                CourseList(
                    onCourseClick = onCourseClick,
                    courseList = item.courses,
                    isExpanded = item.isExpanded,
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
    isExpanded: Boolean,
    modifier: Modifier = Modifier
){
    val density = LocalDensity.current
    AnimatedVisibility(
        visible = isExpanded,
        enter = slideInVertically {
            with(density) { -40.dp.roundToPx() }
        } + expandVertically(
            expandFrom = Alignment.Top
        ) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically() + shrinkVertically() + fadeOut()
    ) {
        Column (modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            if (courseList.isEmpty()) {
                Text("nic tu nie ma")
            } else {
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

