package com.example.mobilnepwr.data

import android.content.Context
import com.example.mobilnepwr.data.courses.CourseRepository
import com.example.mobilnepwr.data.dates.DateRepository
import com.example.mobilnepwr.data.deadlines.DeadlineRepository
import com.example.mobilnepwr.data.images.ImageRepository
import com.example.mobilnepwr.data.notes.OfflineNoteRepository

interface AppContainer {
    val coursesRepository: CourseRepository
    val datesRepository: DateRepository
    val deadlinesRepository: DeadlineRepository
    val imagesRepository: ImageRepository
    val notesRepository: OfflineNoteRepository
}

class AppDataContainer(private val context: Context): AppContainer {
    override val datesRepository: DateRepository by lazy {
        DateRepository(AppDatabase.getDatabase(context).dateDao())
    }
    override val coursesRepository: CourseRepository by lazy {
        CourseRepository(AppDatabase.getDatabase(context).courseDao())
    }

    override val notesRepository: OfflineNoteRepository by lazy {
        OfflineNoteRepository(AppDatabase.getDatabase(context).noteDao())
    }
    override val imagesRepository: ImageRepository by lazy {
        ImageRepository(AppDatabase.getDatabase(context).imageDao())
    }
    override val deadlinesRepository: DeadlineRepository by lazy {
        DeadlineRepository(AppDatabase.getDatabase(context).deadlineDao())
    }
}