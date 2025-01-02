package com.example.mobilnepwr.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector

val items: List<MenuItem> = listOf(
    MenuItem("home","home","home", Icons.Default.Home),
    MenuItem("all_courses","all_courses","all_courses",Icons.Default.Menu),
    MenuItem("import","import","import", Icons.Default.Add),

    )


data class MenuItem(
    val route: String,
    val title: String,
    val contentDescription: String,
    val icon: ImageVector
)