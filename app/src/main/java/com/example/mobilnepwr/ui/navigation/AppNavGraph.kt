package com.example.mobilnepwr.ui.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
import com.example.mobilnepwr.ui.course_deatails.CourseDetailsScreen
import com.example.mobilnepwr.ui.course_deatails.deadline.AddDeadlineScreen
import com.example.mobilnepwr.ui.course_deatails.deadline.EditDeadlineScreen
import com.example.mobilnepwr.ui.course_deatails.note.AddNoteScreen
import com.example.mobilnepwr.ui.course_deatails.note.EditNoteScreen
import com.example.mobilnepwr.ui.courses.AllCoursesScreen
import com.example.mobilnepwr.ui.home.HomeScreen
import com.example.mobilnepwr.ui.import.ImportScreen
import kotlinx.coroutines.launch


@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: ScaffoldViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState by viewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val (onFabClick, setFabOnClick) = remember { mutableStateOf(null as (() -> Unit)?) }
    val context = LocalContext.current
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Column {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = stringResource(R.string.app_name),
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleLarge
                    )
                    menu.forEach { item ->
                        HorizontalDivider()
                        NavigationDrawerItem(
                            label = {
                                Text(text = stringResource(item.titleRes))
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = stringResource(item.contentDescriptionRes)
                                )
                            },
                            onClick = {
                                scope.launch {
                                    drawerState.close()
                                }
                                navController.navigate(item.route)
                            },

                            selected = currentRoute == item.route
                        )

                    }
                }

            }
        },
        drawerState = drawerState,
        scrimColor = Color.Transparent,
        gesturesEnabled = true
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
                            contentDescription = stringResource(R.string.fab_desc),
                            tint = Color.White
                        )
                    }
                }
            },
            modifier = Modifier.clickable(
                enabled = drawerState.isOpen,
                onClick = {
                    scope.launch {
                        drawerState.close()
                    }
                },

                )

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
                            title = context.getString(HomeDestination.titleRes),
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
                            title = context.getString(ImportDestination.titleRes),
                            iconFab = Icons.Default.Info,
                        )
                    }
                    ImportScreen(
                        contentPadding = innerPadding,
                        setFabOnClick = setFabOnClick,
                        navigateBack = navController::navigateUp
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
                            title = context.getString(AllCoursesDestination.titleRes),
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
                        scaffoldViewModel = viewModel,
                        navigateToEditNote = { navController.navigate("${EditNoteDestination.route}/${it}") },
                        navigateToEditDeadline = { navController.navigate("${EditDeadlineDestination.route}/${it}") }

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

                composable(
                    route = EditDeadlineDestination.routeWithArgs,
                    arguments = listOf(navArgument(EditDeadlineDestination.deadlineIdArg) {
                        type = NavType.IntType
                    })
                ) {
                    LaunchedEffect(Unit) {
                        viewModel.updateState(
                            showFab = false,
                            onNavigationIconClick =
                            {
                                navController.navigateUp()
                            },
                            navigationIcon = Icons.Rounded.Clear
                        )
                    }

                    EditDeadlineScreen(
                        navigateBack = { navController.navigateUp() },
                        contentPadding = innerPadding
                    )
                }

                composable(
                    route = EditNoteDestination.routeWithArgs,
                    arguments = listOf(navArgument(EditNoteDestination.noteIdArg) {
                        type = NavType.IntType
                    }
                    ))
                {
                    LaunchedEffect(Unit) {
                        viewModel.updateState(
                            showFab = false,
                            onNavigationIconClick =
                            {
                                navController.navigateUp()
                            },
                            navigationIcon = Icons.Rounded.Clear
                        )
                    }

                    EditNoteScreen(
                        navigateBack = { navController.navigateUp() },
                        contentPadding = innerPadding,
                    )
                }
            }
        }

    }

}

val menu = listOf(
    ImportDestination,
    HomeDestination,
    AllCoursesDestination
)

object ImportDestination : NavigationDestination, MenuItem {
    override val route = "import"
    override val titleRes = R.string.import_title
    override val icon = Icons.Default.KeyboardArrowDown
    override val contentDescriptionRes = R.string.import_desc
}

object HomeDestination : NavigationDestination, MenuItem {
    override val route = "home"
    override val titleRes = R.string.home_title
    override val icon = Icons.Default.Home
    override val contentDescriptionRes = R.string.home_desc
}

object AllCoursesDestination : NavigationDestination, MenuItem {
    override val route = "all_courses"
    override val titleRes = R.string.all_courses_title
    override val icon = Icons.Default.Menu
    override val contentDescriptionRes = R.string.all_courses_desc
}

object CourseDetailsDestination : NavigationDestination {
    override val route = "course_details"
    override val titleRes = R.string.course_details_title
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

object EditDeadlineDestination : NavigationDestination {
    override val route: String = "edit_deadline"
    override val titleRes: Int = R.string.edit_deadline_title
    const val deadlineIdArg = "deadlineId"
    val routeWithArgs = "$route/{$deadlineIdArg}"
}

object EditNoteDestination : NavigationDestination {
    override val route: String = "edit_note"
    override val titleRes: Int = R.string.edit_note_title
    const val noteIdArg = "noteId"
    val routeWithArgs = "$route/{$noteIdArg}"
}

