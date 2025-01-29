package com.example.mobilnepwr.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

open class ScaffoldViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ScaffoldState())
    val uiState = _uiState.asStateFlow()

    open fun updateState(
        showFab: Boolean = _uiState.value.showFab,
        iconFab: ImageVector = _uiState.value.iconFab,
        title: String = "",
        onNavigationIconClick: () -> Unit = _uiState.value.onNavigationIconClick,
        navigationIcon: ImageVector = Icons.Default.Menu,
        enableGestures: Boolean = true
    ) {
        _uiState.update {
            it.copy(
                showFab = showFab,
                iconFab = iconFab,
                title = title,
                onNavigationIconClick = onNavigationIconClick,
                navigationIcon = navigationIcon,
                enableGestures = enableGestures
            )
        }
    }
}

data class ScaffoldState(
    val showFab: Boolean = false,
    val iconFab: ImageVector = Icons.Default.Add,
    val title: String = "",
    val onNavigationIconClick: () -> Unit = {},
    val navigationIcon: ImageVector = Icons.Default.Menu,
    val enableGestures: Boolean = false
)