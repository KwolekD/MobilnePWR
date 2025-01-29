package com.example.mobilnepwr

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mobilnepwr.ui.navigation.AppNavHost

@Composable
fun MobilnePWRApp(
    navController: NavHostController = rememberNavController()
) {
    AppNavHost(navController = navController)
}
