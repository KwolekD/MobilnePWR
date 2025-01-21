package com.example.mobilnepwr.ui.course_deatails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil3.compose.rememberAsyncImagePainter
import com.example.mobilnepwr.R
import com.example.mobilnepwr.data.images.Image
import com.example.mobilnepwr.ui.navigation.ScaffoldViewModel


@Composable
fun Photos(
    photosList: List<Image>,
    setFabOnClick: (() -> Unit) -> Unit,
    onFabClick: () -> Unit,
    scaffoldViewModel: ScaffoldViewModel,
    onDeletePhoto: (Image) -> Unit,
    selectedTab: Int
) {
    var selectedImage by remember { mutableStateOf<Image?>(null) }
    LaunchedEffect(selectedTab) {
        setFabOnClick {
            onFabClick()
        }
        scaffoldViewModel.updateState(
            showFab = true,
            iconFab = Icons.Default.Add,
            navigationIcon = Icons.Default.Clear,
            enableGestures = false
        )
    }


    LazyColumn(modifier = Modifier.padding(10.dp)
        .fillMaxSize()) {
        item {
            Text(
                text = stringResource(R.string.photos_tab_title),
                style = MaterialTheme.typography.titleLarge,
            )
        }
        items(photosList) { image ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { selectedImage = image },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {

                    Image(
                        painter = rememberAsyncImagePainter(model = image.filePath),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Usuń zdjęcie",
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .clickable { onDeletePhoto(image) },
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
    if (selectedImage != null) {
        Dialog(onDismissRequest = { selectedImage = null }) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.8f))
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = selectedImage?.filePath),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clickable { selectedImage = null }
                )
            }
        }
    }
}