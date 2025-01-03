package com.example.mobilnepwr.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.mobilnepwr.R
import com.example.mobilnepwr.ui.AppViewModelProvider
import com.example.mobilnepwr.ui.components.AppBar
import com.example.mobilnepwr.ui.components.items
import com.example.mobilnepwr.ui.course_deatails.CourseDetailsScreen
import com.example.mobilnepwr.ui.course_deatails.deadline.AddDeadlineScreen
import com.example.mobilnepwr.ui.course_deatails.note.AddNoteScreen
import com.example.mobilnepwr.ui.courses.AllCoursesScreen
import com.example.mobilnepwr.ui.home.HomeScreen
import com.example.mobilnepwr.ui.import.ImportScreen
import kotlinx.coroutines.launch


@Composable
fun AppNavHost(
    navController: NavHostController,
    viewModel: ScaffoldViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val (onFabClick, setFabOnClick) = remember { mutableStateOf(null as (() -> Unit)?) }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                items.forEach { item ->
                    NavigationDrawerItem(
                        label = {
                            Text(item.title)
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.contentDescription
                            )
                        },
                        onClick = {
                            scope.launch {
                                drawerState.close()
                            }
                            navController.navigate(item.title)
                        },

                        selected = currentRoute == item.route
                    )

                }
            }
        },
        drawerState = drawerState,
        scrimColor = Color.Transparent
    )
    {
        Scaffold(
            topBar = {
                AppBar(
                    onNavigationIconClick = uiState.onNavigationIconClick,
                    title = uiState.title,
                    navigationIcon = uiState.navigationIcon
                )
            },
            floatingActionButton = {
                if (uiState.showFab) {
                    FloatingActionButton(
                        onClick = { onFabClick?.invoke() },
                        containerColor = Color.Cyan
                    ) {
                        Icon(
                            imageVector = uiState.iconFab,
                            contentDescription = "FAB action",
                            tint = Color.White
                        )
                    }
                } else {
                }
            }

        ) { innerPadding ->

            NavHost(
                navController = navController,
                startDestination = HomeDestination.route,
                modifier = modifier
            ) {
                composable(
                    route = HomeDestination.route
                ) {
                    LaunchedEffect(Unit) {
                        viewModel.updateState(
                            showFab = false,
                            onNavigationIconClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            },
                            title = HomeDestination.route,
                        )
                    }
                    HomeScreen(
                        navigateToCourseDetails = { navController.navigate("${CourseDetailsDestination.route}/${it}") },
                        contentPadding = innerPadding
                    )
                }

                composable(
                    route = ImportDestination.route
                ) {
                    LaunchedEffect(Unit) {
                        viewModel.updateState(
                            showFab = true,
                            onNavigationIconClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            },
                            title = ImportDestination.route,
                            iconFab = Icons.Default.Info,
                        )
                    }
                    ImportScreen(
                        contentPadding = innerPadding,
                        setFabOnClick = setFabOnClick
                    )
                }

                composable(
                    route = AllCoursesDestination.route
                ) {
                    LaunchedEffect(Unit) {
                        viewModel.updateState(
                            showFab = false,
                            onNavigationIconClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            },
                            title = AllCoursesDestination.route,
                        )
                    }
                    AllCoursesScreen(
                        contentPadding = innerPadding,
                        navigateToCourseDetails = { navController.navigate("${CourseDetailsDestination.route}/${it}") })
                }

                composable(
                    route = CourseDetailsDestination.routeWithArgs,
                    arguments = listOf(navArgument(CourseDetailsDestination.courseIdArg) {
                        type = NavType.IntType
                    })
                ) {
                    LaunchedEffect(Unit) {
                        viewModel.updateState(
                            showFab = false,
                            onNavigationIconClick = {
                                navController.navigateUp()
                            },
                            navigationIcon = Icons.Rounded.Clear,
                        )
                    }


                    CourseDetailsScreen(
                        contentPadding = innerPadding,
                        setFabOnClick = setFabOnClick,
                        navigateToAddDeadline = { navController.navigate("${AddDeadlineDestination.route}/${it}") },
                        navigateToAddNote = { navController.navigate("${AddNoteDestination.route}/${it}") },
                        scaffoldViewModel = viewModel

                    )
                }

                composable(
                    route = AddDeadlineDestination.routeWithArgs,
                    arguments = listOf(navArgument(AddDeadlineDestination.courseIdArg) {
                        type = NavType.IntType
                    })
                )
                {
                    LaunchedEffect(Unit) {
                        viewModel.updateState(
                            showFab = false,
                            onNavigationIconClick = {
                                navController.navigateUp()
                            },
                            navigationIcon = Icons.Rounded.Clear,
                        )
                    }
                    AddDeadlineScreen(
                        navigateBack = { navController.navigateUp() },
                        modifier = modifier,
                        contentPadding = innerPadding
                    )
                }

                composable(
                    route = AddNoteDestination.routeWithArgs,
                    arguments = listOf(navArgument(AddNoteDestination.courseIdArg) {
                        type = NavType.IntType
                    })
                )
                {
                    LaunchedEffect(Unit) {
                        viewModel.updateState(
                            showFab = false,
                            onNavigationIconClick = {
                                navController.navigateUp()
                            },
                            navigationIcon = Icons.Rounded.Clear,
                        )
                    }

                    AddNoteScreen(
                        navigateBack = { navController.navigateUp() },
                        modifier = modifier,
                        contentPadding = innerPadding
                    )
                }
            }
        }

    }

}

object ImportDestination : NavigationDestination {
    override val route = "import"
    override val titleRes = R.string.import_title
}

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

object AllCoursesDestination : NavigationDestination {
    override val route = "all_courses"
    override val titleRes = R.string.app_name
}

object CourseDetailsDestination : NavigationDestination {
    override val route = "course_details"
    override val titleRes = R.string.app_name
    const val courseIdArg = "courseId"
    val routeWithArgs = "$route/{$courseIdArg}"
}

object AddDeadlineDestination : NavigationDestination {
    override val route = "add_deadline"
    override val titleRes = R.string.add_deadline_title
    const val courseIdArg = "courseId"
    val routeWithArgs = "$route/{$courseIdArg}"
}

object AddNoteDestination : NavigationDestination {
    override val route = "add_note"
    override val titleRes = R.string.add_note_title
    const val courseIdArg = "courseId"
    val routeWithArgs = "$route/{$courseIdArg}"

}