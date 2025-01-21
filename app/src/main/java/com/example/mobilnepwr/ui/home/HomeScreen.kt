package com.example.mobilnepwr.ui.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilnepwr.R
import com.example.mobilnepwr.data.courses.CourseWithDateDetails
import com.example.mobilnepwr.ui.AppViewModelProvider

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    contentPadding: PaddingValues,
    navigateToCourseDetails: (Int) -> Unit
) {
    val homeUiState by viewModel.homeUiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = viewModel::previousWeek) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(R.string.prev_week_desc)
                )
            }

            IconButton(onClick = viewModel::nextWeek) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowForward,
                    contentDescription = stringResource(R.string.next_week_desc)
                )
            }
        }
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            AnimatedContent(
                targetState = homeUiState.weekDays,
                content = { weekDays ->
                    DayList(
                        daysList = weekDays,
                        onCourseClick = navigateToCourseDetails,
                        onDayClick = viewModel::onDayClick,
                        clickCheckBox = viewModel::clickCheckBox,
                        isExpandedList = homeUiState.isExpandedList,
                        listState = viewModel.lazyListState
                    )
                },
                transitionSpec = {
                    when (homeUiState.animOption) {
                        1 -> {
                            slideInHorizontally(initialOffsetX = { it }) + fadeIn() togetherWith
                                    slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
                        }

                        2 -> {
                            slideInHorizontally(initialOffsetX = { -it }) + fadeIn() togetherWith
                                    slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
                        }

                        else -> {
                            EnterTransition.None togetherWith  ExitTransition.None
                        }
                    }.using(
                        SizeTransform(clip = false)
                    )
                }, label = ""
            )
        }

    }

}

@Composable
fun DayList(
    daysList: List<WeekDay>,
    isExpandedList: List<Boolean>,
    onCourseClick: (Int) -> Unit,
    onDayClick: (Int) -> Unit,
    clickCheckBox: (CourseWithDateDetails) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        state = listState,
    ) {
        itemsIndexed(daysList) { index, item ->
            if (item.courses.isNotEmpty()) {
                ListItem(
                    headlineContent = {
                        Text(
                            text = item.name.replaceFirstChar { it.uppercase() },
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    },
                    supportingContent = {
                        Text(
                            text = item.date,
                            textAlign = TextAlign.Right,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = modifier.then(Modifier
                        .clickable
                        { onDayClick(index) }
                        .clip(ShapeDefaults.Medium)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = ShapeDefaults.Medium
                        ))
                )

                CourseList(
                    onCourseClick = onCourseClick,
                    courseList = item.courses,
                    isExpanded = isExpandedList.get(index),
                    clickCheckBox = clickCheckBox,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun CourseList(
    onCourseClick: (Int) -> Unit,
    courseList: List<CourseWithDateDetails>,
    isExpanded: Boolean,
    clickCheckBox: (CourseWithDateDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    AnimatedVisibility(
        visible = isExpanded,
        enter = slideInVertically { with(density) { -40.dp.roundToPx() } } +
                expandVertically(expandFrom = Alignment.Top) +
                fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically() +
                shrinkVertically() +
                fadeOut()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            courseList.forEachIndexed { index, course ->

                ListItem(
                    headlineContent = {
                        Text(
                            text = course.name,
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    leadingContent = {
                        Text(
                            text = course.type,
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    supportingContent = {
                        Text(
                            text = "${course.startTime} - ${course.endTime}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    trailingContent = {
                        Checkbox(
                            checked = course.attendance,
                            onCheckedChange = { clickCheckBox(course) },
                            colors = CheckboxDefaults.colors()
                                .copy(checkedBoxColor = MaterialTheme.colorScheme.primary)
                        )
                    },
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        headlineColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        leadingIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        trailingIconColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    modifier = Modifier
                        .clickable { onCourseClick(course.courseId) }
                        .padding(vertical = 4.dp)
                        .clip(ShapeDefaults.Medium)
                )

            }
        }
    }
}
