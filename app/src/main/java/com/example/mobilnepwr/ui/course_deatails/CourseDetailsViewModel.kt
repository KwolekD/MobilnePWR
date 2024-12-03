package com.example.mobilnepwr.ui.course_deatails

import androidx.lifecycle.ViewModel
import com.example.mobilnepwr.data.courses.Course

class CourseDetailsViewModel: ViewModel(
)


data class CourseDetails(
    val id: Int = 0,
    val name: String = "",
    val type: String = "",
    val address: String = "",
    val building: String = "",
    val hall: String = ""
)

fun Course.toCourseDetails(): CourseDetails = CourseDetails(
    id = courseId,
    name = name,
    type = type,
    address = address,
    building = building,
    hall = hall
)