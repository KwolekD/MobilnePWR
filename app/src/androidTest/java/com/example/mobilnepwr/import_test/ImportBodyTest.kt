package com.example.mobilnepwr.import_test

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.mobilnepwr.ui.import.ImportBody
import com.example.mobilnepwr.ui.import.ImportUiState
import org.junit.Rule
import org.junit.Test

class ImportBodyTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testImportBody() {

        composeTestRule.setContent {
            ImportBody(
                importUiState = ImportUiState(
                    importLink = "https://example.com/import",
                    showInfo = false,
                    showError = false,
                    showConfirmationDialog = false
                ),
                onImportLinkChange = {},
                onImportClick = {}
            )
        }
        composeTestRule.onNodeWithText("https://example.com/import").assertExists()
        composeTestRule.onNodeWithText("Importuj").assertExists()
        composeTestRule.onNodeWithText("https://example.com/import").assertHasClickAction()
        composeTestRule.onNodeWithText("Importuj").assertHasClickAction()

    }
}