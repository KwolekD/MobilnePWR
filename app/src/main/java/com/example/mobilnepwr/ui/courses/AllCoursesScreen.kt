package com.example.mobilnepwr.ui.courses

import android.widget.Space
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilnepwr.R
import com.example.mobilnepwr.data.courses.Course
import com.example.mobilnepwr.ui.AppViewModelProvider
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

    AllCoursesBody(
        coursesList = courseUiState.classesList,
        onCourseClick = {},
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
                leadingContent = { Text(text = item.type)},
                headlineContent = { Text(text = item.name)},
                trailingContent = { Icon(Icons.Default.Info,"") },
                colors = ListItemDefaults.colors(),
                modifier = Modifier
                    .padding(3.dp)
                    .fillMaxWidth()
                    .clickable {  }

            )
//                Card(
//                    shape = CardDefaults.outlinedShape,
//                    onClick = {onCourseClick},
//                    modifier = Modifier
//                        .padding(3.dp)
//                        .fillMaxWidth()
//                ) {
//                    Row(
//                        modifier = Modifier.align(Alignment.CenterHorizontally)
//                    ) {
//                        Text(text = item.type,
//                            fontSize = 20.sp)
//                        Spacer(modifier = Modifier.padding(2.dp))
//                        Text(text = item.name,
//                            fontSize = 20.sp)
//                    }
//
//                }
                Spacer(
                    modifier = Modifier.padding(2.dp)
                )


            }
        }
}
