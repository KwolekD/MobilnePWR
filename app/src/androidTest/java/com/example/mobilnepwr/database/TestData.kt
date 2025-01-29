package com.example.mobilnepwr.database

import com.example.mobilnepwr.data.courses.Course
import com.example.mobilnepwr.data.dates.Date
import com.example.mobilnepwr.data.deadlines.Deadline
import com.example.mobilnepwr.data.images.Image
import com.example.mobilnepwr.data.notes.Note
import java.time.LocalDate

object TestData {
    val courses = listOf(
        Course(
            courseId = 1,
            name = "Math",
            type = "W",
            address = "street",
            building = "tall",
            hall = "100"
        ),
        Course(
            courseId = 2,
            name = "Polish",
            type = "C",
            address = "streasdet",
            building = "tall",
            hall = "100"
        ),
        Course(
            courseId = 3,
            name = "English",
            type = "P",
            address = "streasdet",
            building = "tall",
            hall = "100"
        )
    )
    val dates = listOf(
        Date(
            courseId = 1,
            date = LocalDate.of(2025, 1, 20),
            startTime = "10:00",
            endTime = "12:00",
            attendance = true
        ),
        Date(
            courseId = 2,
            date = LocalDate.of(2025, 1, 21),
            startTime = "14:00",
            endTime = "16:00",
            attendance = false
        ),
        Date(
            courseId = 3,
            date = LocalDate.of(2025, 1, 22),
            startTime = "09:00",
            endTime = "11:00",
            attendance = true
        ),
        Date(
            courseId = 1,
            date = LocalDate.of(2025, 1, 21),
            startTime = "15:00",
            endTime = "18:00",
            attendance = true
        ),
        Date(
            courseId = 2,
            date = LocalDate.of(2025, 1, 14),
            startTime = "10:00",
            endTime = "12:00",
            attendance = false
        ),
        Date(
            courseId = 3,
            date = LocalDate.of(2025, 1, 29),
            startTime = "14:00",
            endTime = "16:00",
            attendance = true
        ),

        )

    val deadlines = listOf(
        Deadline(
            courseId = 1,
            title = "Math Homework",
            date = LocalDate.of(2025, 2, 10),
            description = "Solve 20 problems"
        ),
        Deadline(
            courseId = 2,
            title = "Physics Lab Report",
            date = LocalDate.of(2025, 2, 12),
            description = "Submit report for Lab 1"
        ),
        Deadline(
            courseId = 3,
            title = "Programming Assignment",
            date = LocalDate.of(2025, 2, 15),
            description = "Build a simple app"
        )
    )

    val notes = listOf(
        Note(
            courseId = 1,
            title = "Algebra Notes",
            date = LocalDate.of(2025, 2, 4),
            content = "Focus on quadratic equations Kotlindddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd"
        ),
        Note(
            courseId = 2,
            title = "Lab Notes",
            date = LocalDate.of(2025, 2, 5),
            content = "Experiment 1 details"
        ),
        Note(
            courseId = 3,
            title = "Lecture Notes",
            date = LocalDate.of(2025, 2, 6),
            content = "OOP principles in Kotlin"
        ),
        Note(
            courseId = 1,
            title = "Algebra Notes 2",
            date = LocalDate.of(2025, 2, 4),
            content = "Focus on quadratic equations"
        )
    )

    val images = listOf(
        Image(courseId = 1, filePath = "/images/math1.png"),
        Image(courseId = 2, filePath = "/images/physics1.png"),
        Image(courseId = 3, filePath = "/images/programming1.png")
    )
    val dayProvider = { LocalDate.of(2025, 1, 20) }
}