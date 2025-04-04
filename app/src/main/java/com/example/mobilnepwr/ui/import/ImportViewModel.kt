package com.example.mobilnepwr.ui.import

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

    fun updateUiState(
        importLink: String,
        showInfo: Boolean = false,
        showConfirmationDialog: Boolean = false
    ) {
        importUiState = importUiState.copy(
            importLink = importLink,
            showInfo = showInfo,
            showConfirmationDialog = showConfirmationDialog
        )
    }

    fun updateUiState(newImportUiState: ImportUiState) {
        importUiState = newImportUiState
    }

    fun clickFAB() {
        updateUiState(importUiState.importLink, showInfo = !importUiState.showInfo)
    }


    suspend fun importData() {
        val client = OkHttpClient()
        val request: Request = try {
            Request.Builder().url(importUiState.importLink).build()
        } catch (e: IllegalArgumentException) {
            importUiState = importUiState.copy(showError = true)
            return
        }
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

                } else {
                    importUiState = importUiState.copy(showError = true)
                    return
                }
            } else {
                importUiState = importUiState.copy(showError = true)
                return
            }
        } catch (e: Exception) {
            importUiState = importUiState.copy(showError = true)
        }
    }

    suspend fun isDatabaseNotEmpty(): Boolean {
        return courseRepository.getAllItemsStream().first().isNotEmpty()
    }
}

data class ImportUiState(
    val importLink: String = "",
    val showInfo: Boolean = false,
    val showConfirmationDialog: Boolean = false,
    val showError: Boolean = false,
)