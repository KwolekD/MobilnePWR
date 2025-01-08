package com.example.mobilnepwr.ui.courses

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilnepwr.data.courses.Course
import com.example.mobilnepwr.ui.AppViewModelProvider


@Composable
fun AllCoursesScreen(
    modifier: Modifier = Modifier,
    viewModel: AllCoursesViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateToCourseDetails: (Int) -> Unit,
    contentPadding: PaddingValues
) {
    val courseUiState by viewModel.courseUiState.collectAsState()

    AllCoursesBody(
        coursesList = courseUiState.classesList,
        onCourseClick = navigateToCourseDetails,
        modifier = Modifier,
        contentPadding = contentPadding
    )

}

@Composable
fun AllCoursesBody(
    coursesList: List<Course>,
    onCourseClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(modifier = Modifier.padding(contentPadding)) {
        items(coursesList) { item ->
            ListItem(
                leadingContent = { Text(text = item.type) },
                headlineContent = { Text(text = item.name) },
                colors = ListItemDefaults.colors(),
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .clickable { onCourseClick(item.courseId) }
                    .clip(MaterialTheme.shapes.medium)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.medium
                    )

            )


        }
    }
}
