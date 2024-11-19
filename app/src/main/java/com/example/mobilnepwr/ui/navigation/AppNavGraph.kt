package com.example.mobilnepwr.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import com.example.mobilnepwr.ui.components.AppDrawer
import com.example.mobilnepwr.ui.course_deatails.CourseDetails
import com.example.mobilnepwr.ui.course_deatails.CourseDetailsScreen
import com.example.mobilnepwr.ui.courses.AllCoursesDestination
import com.example.mobilnepwr.ui.courses.AllCoursesScreen
import com.example.mobilnepwr.ui.home.HomeDestination
import com.example.mobilnepwr.ui.home.HomeScreen
import com.example.mobilnepwr.ui.import.ImportDestination
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
            composable(route = HomeDestination.route) {
                HomeScreen(contentPadding = innerPadding)
            }

            composable(route = ImportDestination.route) {
                ImportScreen(contentPadding = innerPadding)
            }

            composable(route = AllCoursesDestination.route){
                AllCoursesScreen(contentPadding = innerPadding)
            }
            composable(route = CourseDetails.route){
                CourseDetailsScreen(contentPadding = innerPadding)
            }

        }
    }

}