package com.example.mobilnepwr.ui.course_deatails

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilnepwr.data.courses.Course
import com.example.mobilnepwr.data.courses.CourseRepository
import com.example.mobilnepwr.data.dates.Date
import com.example.mobilnepwr.data.dates.DateRepository
import com.example.mobilnepwr.data.deadlines.Deadline
import com.example.mobilnepwr.data.deadlines.DeadlineRepository
import com.example.mobilnepwr.data.images.Image
import com.example.mobilnepwr.data.images.ImageRepository
import com.example.mobilnepwr.data.notes.Note
import com.example.mobilnepwr.data.notes.NoteRepository
import com.example.mobilnepwr.ui.navigation.CourseDetailsDestination
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate

class CourseDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val courseRepository: CourseRepository,
    private val notesRepository: NoteRepository,
    private val deadlineRepository: DeadlineRepository,
    private val dateRepository: DateRepository,
    private val ImageRepository: ImageRepository
) : ViewModel() {

    val courseId: Int = checkNotNull(savedStateHandle[CourseDetailsDestination.courseIdArg])

    val photosList: Flow<List<Image>> = ImageRepository.getAllItemsStream()

    private val _courseUiState: MutableStateFlow<CourseDetailsUiState> =
        MutableStateFlow(CourseDetailsUiState())

    val courseUiState = _courseUiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                courseRepository.getItemStream(courseId),
                deadlineRepository.getDeadlinesByCourseId(courseId),
                notesRepository.getNotesByCourseId(courseId),
                dateRepository.getDatesByCourseId(courseId),
                ImageRepository.getImageByCourseId(courseId)
            ) { course, deadlines, notes, dates, image ->
                _courseUiState.update {
                    it.copy(
                        courseDetails = course.toCourseDetails(),
                        deadlinesList = deadlines.map { deadline -> deadline.toDeadlineDetails() },
                        notesList = notes.map { note -> note.toNoteDetails() } ?: emptyList(),
                        datesList = dates.map { date -> date.toDateDetails() } ?: emptyList(),
                        imageList = image
                    )
                }
            }.collect {}
        }
    }

    fun selectTab(index: Int) {
        _courseUiState.update { currentState ->
            currentState.copy(
                prevSelectedTab = currentState.selectedTab,
                selectedTab = index
            )
        }
    }

    fun updateCheckBox(date: DateDetails) {
        viewModelScope.launch {
            dateRepository.updateDate(date.copy(attendance = !date.attendance).toDate())
        }
    }

    fun savePhoto(context: Context, uri: Uri) {
        val path = saveImageToAppStorage(context, uri)
        if (path != null) {
            viewModelScope.launch {
                ImageRepository.insertItem(
                    Image(
                        courseId = courseId,
                        filePath = path,
                        imageId = 0
                    )
                )
            }
        }
    }

    fun saveImageToAppStorage(context: Context, uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val photoFile = File(context.filesDir, "photo_${System.currentTimeMillis()}.jpg")
            inputStream?.use { input ->
                photoFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            photoFile.absolutePath // Zwrócenie ścieżki zapisanego pliku
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    fun deletePhoto(image: Image) {
        val file = File(image.filePath)
        if (file.exists()) {
            file.delete()
        }
        viewModelScope.launch {
            ImageRepository.deleteItem(image)
        }
    }
}


data class CourseDetailsUiState(
    val courseDetails: CourseDetails = CourseDetails(),
    val selectedTab: Int = 0,
    val prevSelectedTab: Int = -1,
    val notesList: List<NoteDetails> = emptyList(),
    val deadlinesList: List<DeadlineDetails> = emptyList(),
    val datesList: List<DateDetails> = emptyList(),
    val imageList: List<Image> = emptyList()
)

data class NoteDetails(
    val noteId: Int = 0,
    val courseId: Int = 0,
    val title: String = "",
    val content: String = "",
    val date: LocalDate = LocalDate.now()
)


data class CourseDetails(
    val courseId: Int = 0,
    val name: String = "",
    val type: String = "",
    val address: String = "",
    val building: String = "",
    val hall: String = ""
)

data class DeadlineDetails(
    val deadlineId: Int = 0,
    val courseId: Int = 0,
    val title: String = "",
    val description: String = "",
    val date: LocalDate = LocalDate.now()
)

data class DateDetails(
    val dateId: Int = 0,
    val date: LocalDate = LocalDate.now(),
    val attendance: Boolean = true,
    val startTime: String = "",
    val endTime: String = "",
    val courseId: Int = 0
)

fun Course.toCourseDetails(): CourseDetails = CourseDetails(
    courseId = courseId,
    name = name,
    type = type,
    address = address,
    building = building,
    hall = hall
)

fun Deadline.toDeadlineDetails(): DeadlineDetails = DeadlineDetails(
    courseId = courseId,
    title = title,
    description = description,
    date = date,
    deadlineId = deadlineId
)

fun Date.toDateDetails(): DateDetails = DateDetails(
    dateId = dateId,
    date = date,
    attendance = attendance,
    startTime = startTime,
    endTime = endTime,
    courseId = courseId

)

fun DateDetails.toDate(): Date = Date(
    date = date,
    attendance = attendance,
    courseId = courseId,
    startTime = startTime,
    endTime = endTime,
    dateId = dateId
)


fun Note.toNoteDetails(): NoteDetails = NoteDetails(
    courseId = courseId,
    noteId = noteId,
    title = this.title,
    content = this.content,
    date = this.date
)

