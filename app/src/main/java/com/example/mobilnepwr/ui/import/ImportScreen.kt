package com.example.mobilnepwr.ui.import

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilnepwr.ui.AppViewModelProvider
import com.example.mobilnepwr.R
import com.example.mobilnepwr.ui.navigation.NavigationDestination
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch


object ImportDestination: NavigationDestination {
    override val route = "import"
    override val titleRes = R.string.import_title
}


@Composable
fun ImportScreen(
    modifier: Modifier = Modifier,
    viewModel: ImportViewModel = viewModel(factory = AppViewModelProvider.Factory),
    contentPadding: PaddingValues
) {
    val coroutineScope = rememberCoroutineScope()
//    Scaffold (
//        floatingActionButton = {
//            LargeFloatingActionButton(
//                onClick = {coroutineScope.launch {
//                    viewModel.updateUiState(viewModel.importUiState.importLink,true)
//                }},
//                modifier = Modifier
//                    .padding(
//                        end = WindowInsets.safeDrawing.asPaddingValues()
//                            .calculateEndPadding(LocalLayoutDirection.current)
//                    )
//            ) {
//                Icon(
//                    imageVector = Icons.Filled.Info,
//                    contentDescription = "Info about import")
//            }
//        }
//
//    ){  innerPadding ->
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
    if (viewModel.importUiState.showInfo) {
        Dialog(onDismissRequest = {viewModel.updateUiState(viewModel.importUiState.importLink,false)}) {
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
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Jak znaleźć link?",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text("1. Wejdź na stronę swojego USOSa.")
                    Text("2. Otwórz zakładkę MÓJ USOSWEB.")
                    Text("3. Kliknij PLAN ZAJĘĆ.")
                    Text("4. Kliknij eksportuj.")
                    Text("5. Skopiuj link i wklej go do aplikacji.")
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
                onValueChange = {onImportLinkChange(it)},
                label = { Text("Link")},
                maxLines = 1
            )
            Button(
                onClick = {onImportClick()},
                enabled = importUiState.importLink.isNotEmpty(), //do zmiany do wrzucenia do viemodelu chyba?
                modifier = Modifier.fillMaxWidth())
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