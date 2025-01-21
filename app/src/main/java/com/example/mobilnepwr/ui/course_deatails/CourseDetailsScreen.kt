package com.example.mobilnepwr.ui.course_deatails


import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilnepwr.ui.AppViewModelProvider
import com.example.mobilnepwr.ui.course_deatails.deadline.Deadlines
import com.example.mobilnepwr.ui.course_deatails.note.Notes
import com.example.mobilnepwr.ui.navigation.ScaffoldViewModel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun CourseDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: CourseDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    setFabOnClick: (() -> Unit) -> Unit,
    navigateToAddDeadline: (Int) -> Unit,
    navigateToEditDeadline: (Int) -> Unit,
    navigateToAddNote: (Int) -> Unit,
    navigateToEditNote: (Int) -> Unit,
    contentPadding: PaddingValues,
    scaffoldViewModel: ScaffoldViewModel
) {
    val courseUiState by viewModel.courseUiState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${courseUiState.courseDetails.type} - ${courseUiState.courseDetails.name}",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val tabs = listOf("Szczegóły", "Notatki", "Terminy", "Obecność", "Zdjęcia")

        PrimaryScrollableTabRow(
            edgePadding = 0.dp,
            tabs = {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = courseUiState.selectedTab == index,
                        onClick = { viewModel.selectTab(index) },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelLarge
                            )
                        })
                }
            },
            selectedTabIndex = courseUiState.selectedTab

        )
        val context = LocalContext.current
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = { uri ->
                uri?.let {
                    viewModel.savePhoto(
                        uri = it,
                        context = context
                    )
                }
            }
        )

        AnimatedContent(
            targetState = courseUiState.selectedTab,
            transitionSpec = {
                if (courseUiState.prevSelectedTab < courseUiState.selectedTab) {
                    slideInHorizontally(initialOffsetX = { it }) + fadeIn() togetherWith
                            slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
                } else {
                    slideInHorizontally(initialOffsetX = { -it }) + fadeIn() togetherWith
                            slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
                }
            }
        ) { targetState ->

            when (targetState) {
                0 -> Details(
                        courseUiState.courseDetails,
                        scaffoldViewModel,
                        targetState,
                    )

                1 -> Notes(
                    notesList = courseUiState.notesList,
                    setFabOnClick = setFabOnClick,
                    onFabClick = { navigateToAddNote(viewModel.courseId) },
                    scaffoldViewModel = scaffoldViewModel,
                    onEditClick = navigateToEditNote,
                    onDeleteClick = viewModel::deleteNote,
                    clickNote = viewModel::clickNote,
                    selectedTab = targetState
                )

                2 -> Deadlines(
                    deadlinesList = courseUiState.deadlinesList,
                    setFabOnClick = setFabOnClick,
                    onFabClick = { navigateToAddDeadline(viewModel.courseId) },
                    scaffoldViewModel = scaffoldViewModel,
                    onEditClick = navigateToEditDeadline,
                    onDeleteClick = viewModel::deleteDeadline,
                    selectedTab = targetState
                )

                3 -> Dates(
                    datesList = courseUiState.datesList,
                    scaffoldViewModel = scaffoldViewModel,
                    viewModel = viewModel,
                    selectedTab = targetState
                )

                4 -> Photos(
                    photosList = courseUiState.imageList,
                    setFabOnClick = setFabOnClick,
                    onFabClick = { launcher.launch("image/*") },
                    scaffoldViewModel = scaffoldViewModel,
                    onDeletePhoto = viewModel::deletePhoto,
                    selectedTab = targetState
                )
            }
        }
    }

}











