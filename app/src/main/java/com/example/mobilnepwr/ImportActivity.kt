package com.example.mobilnepwr

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import biweekly.Biweekly
import biweekly.property.Summary
import com.example.mobilnepwr.database.AppDatabase
import com.example.mobilnepwr.database.ClassEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale
import androidx.lifecycle.lifecycleScope

class ImportActivity :ComponentActivity() {
//    private val database: AppDatabase by lazy {
//        (application as MyApp).database
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleFilledTextFieldSample()
        }

    }

}

@Composable
fun SimpleFilledTextFieldSample() {
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }
    var showDialog by remember{ mutableStateOf(false)}
    var importSuccess by remember { mutableIntStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

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
                onClick = {
                    if (text.isNotEmpty()){
                        coroutineScope.launch {
                            importSuccess = if(importData(context = context , url = text)){
                                1
                            } else{
                                2
                            }
                        }

                    }


                }
            )
            {
                Text(
                    text = "Importuj",
                    color = Color.White,
                    fontSize = 20.sp,
                )
            }

            if (importSuccess == 1){
                Text(
                    text = "sukces"
                )
            }
            else if (importSuccess == 2){
                Text(
                    text = "błąd"
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
            Dialog(onDismissRequest = { showDialog = false }) {
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
}

suspend fun importData(context: Context, url: String): Boolean
{
    val client = OkHttpClient()
    val request = Request.Builder().url(url).build()

    try {
        val response = withContext(Dispatchers.IO) { client.newCall(request).execute() }
        if (response.isSuccessful) {
            val body = response.body
            if (body != null) {
                val tempFile = withContext(Dispatchers.IO) {
                    File.createTempFile("calendar", ".ics")
                }
                tempFile.writeBytes(body.bytes())

                // Parsowanie pliku ICS
                val ical = Biweekly.parse(tempFile).first()
                val events = ical.events.map { event ->

                    val dateTimeStart = event.dateStart.value
                    val localDateTimeStart = LocalDateTime.ofInstant(dateTimeStart.toInstant(),
                        ZoneId.systemDefault())

                    val timeStart = localDateTimeStart.toLocalTime().toString()
                    val dayOfWeek = localDateTimeStart.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("pl"))

                    val dateTimeEnd = event.dateEnd.value
                    val localDateTimeEnd = LocalDateTime.ofInstant(dateTimeEnd.toInstant(),
                        ZoneId.systemDefault())

                    val timeEnd = localDateTimeEnd.toLocalTime().toString()
                    ClassEntity(
                        type = getType(event.summary),
                        name = getName(event.summary),
                        timeStart = timeStart,
                        timeEnd = timeEnd,
                        day = dayOfWeek,
                        address = event.location?.value ?: "",
                        building = event.description.value.split(" ")[1],
                        hall = event.description.value.split(" ")[0]
                    )
                }

                // Zapis danych do Room
                val db = (context.applicationContext as MyApp).database
                db.classDao().insertAll(events)



                tempFile.delete()
            }
        } else {
            return false
        }
        return true
    } catch (e: Exception) {
        return false
    }
}

fun getType(summary: Summary): String{
    return summary.value.split(" ").first()
}

fun getName(summary: Summary): String{
    return summary.value.split(" ")[2]
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewSimpleTextField(){
//    SimpleFilledTextFieldSample(data)
//}