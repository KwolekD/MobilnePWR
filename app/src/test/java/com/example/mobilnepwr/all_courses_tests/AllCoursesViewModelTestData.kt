package com.example.mobilnepwr.all_courses_tests

import com.example.mobilnepwr.data.courses.Course

object AllCoursesViewModelTestData {
    val courses1 = listOf(
        Course(
            name = "Matematyka",
            type = "W",
            courseId = 1,
            address = "",
            building = "",
            hall = "",
        ),
        Course(
            name = "Polski",
            type = "P",
            courseId = 2,
            address = "",
            building = "",
            hall = "",
        ),
        Course(
            name = "Angielski",
            type = "C",
            courseId = 3,
            address = "",
            building = "",
            hall = "",
        ),
    )

    val courses2 = listOf<Course>()
}