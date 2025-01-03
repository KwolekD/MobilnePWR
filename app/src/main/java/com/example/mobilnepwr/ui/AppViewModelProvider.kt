package com.example.mobilnepwr.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mobilnepwr.MobilnePWRApplication
import com.example.mobilnepwr.ui.course_deatails.CourseDetailsViewModel
import com.example.mobilnepwr.ui.course_deatails.deadline.AddDeadlineViewModel
import com.example.mobilnepwr.ui.course_deatails.note.AddNoteViewModel
import com.example.mobilnepwr.ui.courses.AllCoursesViewModel
import com.example.mobilnepwr.ui.home.HomeViewModel
import com.example.mobilnepwr.ui.import.ImportViewModel
import com.example.mobilnepwr.ui.navigation.ScaffoldViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(mobilnePWRApplication().container.coursesRepository)
        }

        initializer {
            ImportViewModel(
                mobilnePWRApplication().container.datesRepository,
                mobilnePWRApplication().container.coursesRepository
            )
        }

        initializer {
            AllCoursesViewModel(mobilnePWRApplication().container.coursesRepository)
        }

        initializer {
            CourseDetailsViewModel(
                this.createSavedStateHandle(),
                mobilnePWRApplication().container.coursesRepository,
                mobilnePWRApplication().container.notesRepository,
                mobilnePWRApplication().container.deadlinesRepository,
                mobilnePWRApplication().container.datesRepository
            )
        }

        initializer {
            AddDeadlineViewModel(
                this.createSavedStateHandle(),
                mobilnePWRApplication().container.deadlinesRepository
            )
        }

        initializer {
            ScaffoldViewModel()
        }

        initializer {
            AddNoteViewModel(
                this.createSavedStateHandle(),
                mobilnePWRApplication().container.notesRepository
            )
        }
    }
}

fun CreationExtras.mobilnePWRApplication(): MobilnePWRApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as MobilnePWRApplication)