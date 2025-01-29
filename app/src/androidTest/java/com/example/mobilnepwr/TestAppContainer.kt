package com.example.mobilnepwr

import android.content.Context
import androidx.room.Room
import com.example.mobilnepwr.data.AppContainer
import com.example.mobilnepwr.data.AppDatabase
import com.example.mobilnepwr.data.courses.CourseRepository
import com.example.mobilnepwr.data.dates.DateRepository
import com.example.mobilnepwr.data.deadlines.DeadlineRepository
import com.example.mobilnepwr.data.images.ImageRepository
import com.example.mobilnepwr.data.notes.NoteRepository
import com.example.mobilnepwr.database.TestData
import kotlinx.coroutines.runBlocking

class TestAppContainer(context: Context) : AppContainer {
    private val database: AppDatabase = Room.inMemoryDatabaseBuilder(
        context,
        AppDatabase::class.java
    ).allowMainThreadQueries().build()

    override val datesRepository: DateRepository by lazy {
        DateRepository(database.dateDao())
    }

    override val coursesRepository: CourseRepository by lazy {
        CourseRepository(database.courseDao())
    }

    override val notesRepository: NoteRepository by lazy {
        NoteRepository(database.noteDao())
    }

    override val imagesRepository: ImageRepository by lazy {
        ImageRepository(database.imageDao())
    }

    override val deadlinesRepository: DeadlineRepository by lazy {
        DeadlineRepository(database.deadlineDao())
    }

    fun fillTestDataBase() {
        runBlocking {
            TestData.courses.forEach { database.courseDao().insertClass(it) }
            TestData.dates.forEach { database.dateDao().insertDate(it) }
            TestData.deadlines.forEach { database.deadlineDao().insertDeadline(it) }
            TestData.notes.forEach { database.noteDao().insertNote(it) }
            TestData.images.forEach { database.imageDao().insertImage(it) }
        }
    }

    suspend fun clearDataBase() {
        coursesRepository.clearDatabase()
    }
}
