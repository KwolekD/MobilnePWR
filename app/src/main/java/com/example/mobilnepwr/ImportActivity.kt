package com.example.mobilnepwr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import okhttp3.OkHttpClient
import okhttp3.Request

class ImportActivity :ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleFilledTextFieldSample()
        }

    }

}
@Composable
fun SimpleFilledTextFieldSample() {
    var text by remember { mutableStateOf("") }
    var showDialog by remember{ mutableStateOf(false)}
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 72.dp)
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { newText -> text = newText },
                label = { Text("Link")},
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* TODO */ }
            ) {
                Text(
                    text = "Importuj",
                    color = Color.White,
                    fontSize = 20.sp,
                )
            }
        }
        LargeFloatingActionButton(
            onClick = {
                showDialog = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = "Info about import",
                Modifier.size(50.dp))
        }
        if (showDialog) {
            Dialog(
                onDismissRequest = {showDialog = false}
            )
            {
                Card {

                }
            }
        }
    }
}

suspend fun GetData(URL: String)
{
    val client = OkHttpClient()
    val requst = Request.Builder().url(URL).build()

    try {

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSimpleTextField(){
    SimpleFilledTextFieldSample()
}