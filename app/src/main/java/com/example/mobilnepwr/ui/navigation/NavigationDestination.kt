package com.example.mobilnepwr.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector

interface NavigationDestination {
    val route: String
    val titleRes: Int
}

interface MenuItem : NavigationDestination {
    val icon: ImageVector
    val contentDescriptionRes: Int
}