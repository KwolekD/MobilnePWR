package com.example.mobilnepwr

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import com.example.mobilnepwr.ui.navigation.ScaffoldState
import com.example.mobilnepwr.ui.navigation.ScaffoldViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ScaffoldViewModelTest {
    private lateinit var viewModel: ScaffoldViewModel

    @Before
    fun setUp() {
        viewModel = ScaffoldViewModel()
    }

    @Test
    fun `updateState should update the UI state correctly`() {
        val showFab = true
        val iconFab = Icons.Default.Add
        val title = "Test Title"
        val onNavigationIconClick = {}
        val navigationIcon = Icons.Default.Add
        val enableGestures = true

        viewModel.updateState(
            showFab = showFab,
            iconFab = iconFab,
            title = title,
            onNavigationIconClick = onNavigationIconClick,
            navigationIcon = navigationIcon,
            enableGestures = enableGestures
        )

        assert(viewModel.uiState.value.showFab == showFab)
        assert(viewModel.uiState.value.iconFab == iconFab)
        assert(viewModel.uiState.value.title == title)
        assert(viewModel.uiState.value.onNavigationIconClick == onNavigationIconClick)
        assert(viewModel.uiState.value.navigationIcon == navigationIcon)
        assert(viewModel.uiState.value.enableGestures == enableGestures)

        viewModel.updateState()

        assertEquals(
            viewModel.uiState.value, ScaffoldState(
                showFab = showFab,
                iconFab = iconFab,
                title = "",
                onNavigationIconClick = onNavigationIconClick,
                navigationIcon = Icons.Default.Menu,
                enableGestures = true
            )
        )
    }
}