package com.example.mobilnepwr.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    onNavigationIconClick: () -> Unit,
    title: String,
    navigationIcon: ImageVector
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onNavigationIconClick
            ) {
                Icon(
                    imageVector = navigationIcon,
                    contentDescription = "Navigation icon",
                    modifier = Modifier.size(34.dp)
                )
            }
        }
    )
}

