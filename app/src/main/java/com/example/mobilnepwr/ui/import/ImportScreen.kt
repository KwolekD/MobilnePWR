package com.example.mobilnepwr.ui.import

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilnepwr.R
import com.example.mobilnepwr.ui.AppViewModelProvider
import kotlinx.coroutines.launch


@Composable
fun ImportScreen(
    modifier: Modifier = Modifier,
    viewModel: ImportViewModel = viewModel(factory = AppViewModelProvider.Factory),
    setFabOnClick: (() -> Unit) -> Unit,
    navigateBack: () -> Unit,
    contentPadding: PaddingValues
) {
    val coroutineScope = rememberCoroutineScope()
    val importUiState = viewModel.importUiState
    ImportBody(
        importUiState = importUiState,
        onImportLinkChange = viewModel::updateUiState,
        onImportClick = {
            coroutineScope.launch {
                if (viewModel.isDatabaseNotEmpty()) {
                    viewModel.updateUiState(
                        importLink = importUiState.importLink,
                        showConfirmationDialog = true
                    )
                } else {
                    viewModel.importData()
                    if (!viewModel.importUiState.showError)
                        navigateBack()


                }
            }
        },
        modifier = Modifier
            .padding(
                start = contentPadding.calculateStartPadding(LocalLayoutDirection.current),
                top = contentPadding.calculateTopPadding(),
                end = contentPadding.calculateEndPadding(LocalLayoutDirection.current),
            )
            .fillMaxWidth()
    )

    LaunchedEffect(Unit) {
        setFabOnClick {
            viewModel.clickFAB()
        }
    }
    if (importUiState.showInfo) {
        Dialog(onDismissRequest = {
            viewModel.updateUiState(
                importUiState.importLink,
                showInfo = false
            )
        }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(10.dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = stringResource(R.string.fab_import_title),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    Text(
                        stringResource(R.string.fab_import_step_1),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        stringResource(R.string.fab_import_step_2),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        stringResource(R.string.fab_import_step_3),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        stringResource(R.string.fab_import_step_4),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        stringResource(R.string.fab_import_step_5),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
    if (importUiState.showError) {
        AlertDialog(
            title = { Text(stringResource(R.string.import_error_title)) },
            onDismissRequest = {
                viewModel.updateUiState(
                    importUiState.copy(showError = false)
                )
            },
            text = { Text(stringResource(R.string.import_error_text)) },
            confirmButton = {
                Button(onClick = {
                    viewModel.updateUiState(importUiState.copy(showError = false))
                }) {
                    Text(stringResource(R.string.ok_button_title))
                }
            },


            )
    }
    if (importUiState.showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = {
                viewModel.updateUiState(
                    importUiState.importLink,
                    showConfirmationDialog = false
                )
            },
            title = { Text(stringResource(R.string.import_confirm_title)) },
            text = {
                Text(stringResource(R.string.import_confirm_text))
            },
            confirmButton = {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.updateUiState(
                                importUiState.importLink,
                                showConfirmationDialog = false,
                            )
                            viewModel.importData()
                            if (!viewModel.importUiState.showError) {
                                navigateBack()
                            }

                        }
                    }
                ) {
                    Text(stringResource(R.string.yes_button_title))
                }
            },
            dismissButton = {
                Button(onClick = {
                    viewModel.updateUiState(
                        importUiState.importLink,
                        showConfirmationDialog = false
                    )
                }) {
                    Text(stringResource(R.string.no_button_title))
                }
            }
        )
    }

}


@Composable
fun ImportBody(
    importUiState: ImportUiState,
    onImportLinkChange: (String) -> Unit,
    onImportClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 72.dp)
        ) {
            OutlinedTextField(
                value = importUiState.importLink,
                onValueChange = { onImportLinkChange(it) },
                label = { Text(stringResource(R.string.link_label)) },
                maxLines = 1
            )
            Button(
                onClick = { onImportClick() },
                enabled = importUiState.importLink.isNotEmpty(),
                modifier = Modifier
            )
            {
                Text(
                    text = stringResource(R.string.import_button_title),
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        }

    }
}