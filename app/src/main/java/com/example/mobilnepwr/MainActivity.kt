package com.example.mobilnepwr

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.example.mobilnepwr.ui.theme.MobilnePWRTheme
import com.example.mobilnepwr.ImportActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyButtonScreen()
        }
    }
}

@Composable
fun MyButtonScreen() {
    val context = LocalContext.current
    // Główne ustawienie layoutu na środku ekranu
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top, // Ustawienie przycisków na środku pionowo
        horizontalAlignment = Alignment.CenterHorizontally // Ustawienie przycisków na środku poziomo
    ) {
        // Trzy przyciski
        Button(
            onClick = {
                val intent = Intent(context, ImportActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text("Przycisk 1")
        }
        Button(
            onClick = { /* Brak akcji */ },
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text("Przycisk 2")
        }
        Button(
            onClick = { /* Brak akcji */ },
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text("Przycisk 3")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMyButtonScreen() {
    MyButtonScreen()
}