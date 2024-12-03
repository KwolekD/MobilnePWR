package com.example.mobilnepwr.ui

import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mobilnepwr.ui.import.ImportViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import com.example.mobilnepwr.MobilnePWRApplication
import com.example.mobilnepwr.data.courses.Course
import com.example.mobilnepwr.ui.course_deatails.CourseDetailsViewModel
import com.example.mobilnepwr.ui.courses.AllCoursesViewModel
import com.example.mobilnepwr.ui.home.HomeViewModel
import com.example.mobilnepwr.ui.navigation.CourseDetailsDestination

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(mobilnePWRApplication().container.coursesRepository)
        }

        initializer {
            ImportViewModel(mobilnePWRApplication().container.datesRepository,
                mobilnePWRApplication().container.coursesRepository)
        }

        initializer {
            AllCoursesViewModel(mobilnePWRApplication().container.coursesRepository)
        }

        initializer {
            CourseDetailsViewModel()
        }
    }
}

fun CreationExtras.mobilnePWRApplication(): MobilnePWRApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as MobilnePWRApplication)