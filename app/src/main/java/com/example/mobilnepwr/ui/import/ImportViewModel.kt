package com.example.mobilnepwr.ui.import

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import biweekly.Biweekly
import com.example.mobilnepwr.data.courses.CourseRepository
import com.example.mobilnepwr.data.dates.DateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File

class ImportViewModel(
    private val dateRepository: DateRepository,
    private val courseRepository: CourseRepository
) : ViewModel() {
    var importUiState by mutableStateOf(ImportUiState())
        private set

    fun updateUiState(importLink: String, showInfo: Boolean = false) {
        importUiState =
            ImportUiState(
                importLink = importLink,
                showInfo = showInfo
            )
    }

    fun clickFAB() {
        updateUiState(importUiState.importLink, showInfo = !importUiState.showInfo)
    }


    suspend fun importData() {
        val client = OkHttpClient()
        val request = Request.Builder().url(importUiState.importLink).build()
        Log.d("import", "działa")
        try {
            val response = withContext(Dispatchers.IO) { client.newCall(request).execute() }
            if (response.isSuccessful) {
                val body = response.body
                if (body != null) {
                    val tempFile = withContext(Dispatchers.IO) {
                        File.createTempFile("calendar", ".ics")
                    }
                    tempFile.writeBytes(body.bytes())

                    val ical = Biweekly.parse(tempFile).first()

                    courseRepository.importCoursesFromIcal(ical)
                    dateRepository.importDatesFromIcal(
                        ical,
                        courseRepository.getAllItemsStream().first()
                    )
                    tempFile.delete()
                }
            }
            Log.d("import", response.message)
        } catch (e: Exception) {
            e.message?.let { Log.e("Import", it) }
        }
    }
}

data class ImportUiState(
    val importLink: String = "",
    val showInfo: Boolean = false
)