package com.example.mobilnepwr.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    onNavigationIconClick: () -> Unit,
    title: String,
    navigationIcon: ImageVector
) {
    TopAppBar(
        title = { Text(
            text = title
        )},
        navigationIcon = {
            IconButton(
                onClick = onNavigationIconClick
            ) {
                Icon(
                    imageVector = navigationIcon,
                    contentDescription = "Navigation icon"
                )
            }
        }
    )
}