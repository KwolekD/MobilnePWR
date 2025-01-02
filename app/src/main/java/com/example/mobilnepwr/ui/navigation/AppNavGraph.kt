package com.example.mobilnepwr.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.mobilnepwr.R
import com.example.mobilnepwr.ui.components.AppBar
import com.example.mobilnepwr.ui.components.items
import com.example.mobilnepwr.ui.course_deatails.CourseDetailsScreen
import com.example.mobilnepwr.ui.courses.AllCoursesScreen
import com.example.mobilnepwr.ui.home.HomeScreen
import com.example.mobilnepwr.ui.import.ImportScreen
import kotlinx.coroutines.launch


@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                items.forEach { item ->
                    NavigationDrawerItem(
                        label = {
                            Text(item.title)
                        },
                        icon = {
                            Icon(imageVector = item.icon, contentDescription = item.contentDescription)
                        },
                        onClick = {
                            scope.launch {
                                drawerState.close()
                            }
                            navController.navigate(item.title)
                        },

                        selected = false
                    )

                }
            }
        },
        drawerState = drawerState,
        scrimColor = Color.Transparent
    )
    {
        Scaffold (
            topBar = {
                AppBar(
                    onNavigationIconClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    title =
                        when (currentRoute) {
                            HomeDestination.route -> "Home"
                            ImportDestination.route -> "Import"
                            AllCoursesDestination.route -> "All courses"
                            CourseDetailsDestination.route -> ""
                            else -> "Mobilne PWR"
                        },
                    navigationIcon = Icons.Default.Menu
                )
            },
            floatingActionButton = {
                when (currentRoute) {
                    ImportDestination.route ->
                        FloatingActionButton(
                        onClick = { /*TODO*/ },
                        containerColor = Color.Blue
                    ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "ImportInfo",
                                tint = Color.White
                            )
                        }
                    else -> {}
                }
            }

        ){ innerPadding ->

            NavHost(
                navController = navController,
                startDestination = HomeDestination.route,
                modifier = modifier
            ) {
                composable(
                    route = HomeDestination.route
                ) {
                    HomeScreen(
                        navigateToCourseDetails = { navController.navigate("${CourseDetailsDestination.route}/${it}") },
                        contentPadding = innerPadding
                    )
                }

                composable(
                    route = ImportDestination.route
                ) {
                    ImportScreen(contentPadding = innerPadding)
                }

                composable(
                    route = AllCoursesDestination.route
                ) {
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

                    CourseDetailsScreen(contentPadding = innerPadding)
                }

            }
        }

    }


}

object ImportDestination: NavigationDestination {
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
