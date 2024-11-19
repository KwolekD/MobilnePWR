package com.example.mobilnepwr.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilnepwr.ui.AppViewModelProvider
import com.example.mobilnepwr.R
import com.example.mobilnepwr.data.courses.Course
import com.example.mobilnepwr.ui.navigation.NavigationDestination
import kotlinx.coroutines.flow.StateFlow

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    contentPadding: PaddingValues
){
    val homeUiState by viewModel.homeUiState.collectAsState()
    HomeBody(
        daysList = homeUiState.weekDayButtonList,
        modifier = modifier.fillMaxSize(),
        onCourseClick = {},
        contentPadding = contentPadding)
}


@Composable
fun HomeBody(
    daysList: List<WeekDayButton>,
    onCourseClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
){
    LazyColumn(modifier.padding(contentPadding)) {
        items(daysList){item ->
            DayList(
                name = item.name,
                onDayClick = {},
                courseList = item.classesList,
                show = item.clicked,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun DayList(
    name: String,
    onDayClick: () -> Unit,
    courseList: StateFlow<List<Course>>,
    show: Boolean,
    modifier: Modifier = Modifier
){
//    Button(
//        onClick = onDayClick,
//        colors = ButtonColors(
//            contentColor = Color.White,
//            containerColor = Color.Black,
//            disabledContentColor = Color.White,
//            disabledContainerColor = Color.Black),
//        shape = RoundedCornerShape(8.dp),
//        modifier = modifier
//            .padding(10.dp)
//    ) {
//        Text(text = name,
//            fontSize = 20.sp)
//    }
    ListItem(
        headlineContent = { Text(
            text = name,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center)},
        modifier = modifier.then(Modifier
            .clickable { onDayClick }
            .padding(20.dp)
            .padding(vertical = 35.dp)
    ))
    val courseList by courseList.collectAsState()

    Column (modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally){
        courseList.forEach{ course ->
            if (show){
                ListItem(
                    headlineContent = { Text(text = course.name)},
                    leadingContent = { Text(text = course.type)},
                    modifier = Modifier
                        .clickable {  })
            }
        }
    }
}

