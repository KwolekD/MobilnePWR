package com.example.mobilnepwr.ui.course_deatails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobilnepwr.ui.components.Icons.Sensor_door
import com.example.mobilnepwr.ui.navigation.ScaffoldViewModel


@Composable
fun Details(

    courseDetails: CourseDetails,
    scaffoldViewModel: ScaffoldViewModel,
    itemModifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
) {
    LaunchedEffect(Unit) {
        scaffoldViewModel.updateState(
            showFab = false,
            navigationIcon = Icons.Default.Clear,
            enableGestures = false
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val items = listOf(
            Triple(Icons.Rounded.AccountBox, courseDetails.name, "name"),
            Triple(
                Icons.Rounded.Info,
                when (courseDetails.type) {
                    "L" -> "Laboratorium"
                    "P" -> "Projekt"
                    "C" -> "Ćwiczenia"
                    "W" -> "Wykład"
                    "S" -> "Seminarium"
                    else -> ""

                }, "type"
            ),
            Triple(Sensor_door, courseDetails.hall, "hall"),
            Triple(Icons.Rounded.Home, courseDetails.building, "building"),
            Triple(Icons.Rounded.Place, courseDetails.address, "address")
        )

        items.forEach { (icon, text, description) ->
            ListItem(
                leadingContent = {
                    Icon(
                        imageVector = icon,
                        contentDescription = description,
                        modifier = Modifier
                            .size(24.dp)
                    )
                },
                headlineContent = {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                modifier = itemModifier
            )
        }
    }
}