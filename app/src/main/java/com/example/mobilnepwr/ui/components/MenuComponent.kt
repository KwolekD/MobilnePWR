package com.example.mobilnepwr.ui.components

import android.view.Menu
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.material.color.utilities.Scheme
import com.google.android.material.color.utilities.SchemeContent
import kotlinx.coroutines.launch

val items: List<MenuItem> = listOf(
    MenuItem("import","import","import", Icons.Default.Add),
    MenuItem("home","home","home", Icons.Default.Home),
    MenuItem("all_courses","all_courses","all_courses",Icons.Default.Menu)
)


@Composable
fun AppDrawer(
    modifier: Modifier = Modifier,
    navController: NavController,
    content: @Composable (contentPadding: PaddingValues) -> Unit){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
            items.forEachIndexed {index, item ->
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
                        navController.navigate(item.title)},

                    selected = false
                )

            }
        }},
        drawerState = drawerState,
        scrimColor = Color.Transparent
    ) {
        Scaffold (
            modifier = modifier,
            topBar = { AppBar(
                onNavigationIconClick = {scope.launch {
                    drawerState.open()
                }}
            )}
        )
        {innerPadding ->
            content(innerPadding)
        }
    }
}

@Composable
fun AppDrawerContent(
    navController: NavController,
    modifier: Modifier){
    DrawerBody(
        listOf(
            MenuItem("import","import","import", Icons.Default.Add),
            MenuItem("home","home","home", Icons.Default.Home)),
        navController = navController,
        modifier = modifier)

}

@Composable
fun DrawerBody(
    items: List<MenuItem>,
    modifier: Modifier = Modifier,
    navController: NavController
){
    LazyColumn(modifier.background(Color.White)
        .fillMaxSize()) {
        items(items) { item ->
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .clickable {navController.navigate(item.title)}
                    .padding(16.dp),
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.contentDescription)
                Spacer(modifier = modifier.width(16.dp))
                Text(
                    text = item.title,
                    modifier = modifier.weight(1f)
                )
            }

        }
    }

}

data class MenuItem(
    val id: String,
    val title: String,
    val contentDescription: String,
    val icon: ImageVector
)