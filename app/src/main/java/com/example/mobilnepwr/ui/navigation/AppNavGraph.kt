package com.example.mobilnepwr.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.navArgument
import com.example.mobilnepwr.R
import com.example.mobilnepwr.ui.components.AppDrawer
import com.example.mobilnepwr.ui.course_deatails.CourseDetailsScreen
import com.example.mobilnepwr.ui.courses.AllCoursesScreen
import com.example.mobilnepwr.ui.home.HomeScreen
import com.example.mobilnepwr.ui.import.ImportScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    AppDrawer(
        navController = navController
    ) {innerPadding ->
        NavHost(
            navController = navController,
            startDestination = HomeDestination.route,
            modifier = modifier
        ) {
            composable(
                route = HomeDestination.route
            ) {
                HomeScreen(
                    contentPadding = innerPadding,
                    navigateToCourseDetails = {}
                    )
            }

            composable(
                route = ImportDestination.route
            ){
                ImportScreen(contentPadding = innerPadding)
            }

            composable(
                route = AllCoursesDestination.route
            ){
                AllCoursesScreen(contentPadding = innerPadding)
            }

            composable(
                route = CourseDetailsDestination.routeWithArgs,
                arguments = listOf(navArgument(CourseDetailsDestination.courseIdArg) {
                    type = NavType.IntType
                })
            ){
                CourseDetailsScreen(contentPadding = innerPadding)
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
