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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    contentPadding: PaddingValues
) {
    val coroutineScope = rememberCoroutineScope()
    ImportBody(
        importUiState = viewModel.importUiState,
        onImportLinkChange = viewModel::updateUiState,
        onImportClick = {
            coroutineScope.launch {
                viewModel.importData()
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
    if (viewModel.importUiState.showInfo) {
        Dialog(onDismissRequest = {
            viewModel.updateUiState(
                viewModel.importUiState.importLink,
                false
            )
        }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(16.dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = stringResource(R.string.fab_import_title),
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    Text(stringResource(R.string.fab_import_step_1))
                    Text(stringResource(R.string.fab_import_step_2))
                    Text(stringResource(R.string.fab_import_step_3))
                    Text(stringResource(R.string.fab_import_step_4))
                    Text(stringResource(R.string.fab_import_step_5))
                }
            }
        }
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
                label = { Text("Link") },
                maxLines = 1
            )
            Button(
                onClick = { onImportClick() },
                enabled = importUiState.importLink.isNotEmpty(),
                modifier = Modifier
            )
            {
                Text(
                    text = "Importuj",
                    color = Color.White,
                    fontSize = 20.sp,
                )
            }
        }

    }
}