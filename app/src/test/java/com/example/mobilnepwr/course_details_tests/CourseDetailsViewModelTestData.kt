package com.example.mobilnepwr.course_details_tests

import com.example.mobilnepwr.data.courses.Course
import com.example.mobilnepwr.data.dates.Date
import com.example.mobilnepwr.data.deadlines.Deadline
import com.example.mobilnepwr.data.images.Image
import com.example.mobilnepwr.data.notes.Note
import java.time.LocalDate

object CourseDetailsViewModelTestData {
    val course1 = Course(
        courseId = 1,
        name = "Matematyka",
        type = "W",
        building = "D.2",
        hall = "203",
        address = "ul. Piłsudskiego 12"
    )

    val course1Notes = listOf(
        Note(
            courseId = 1,
            noteId = 1,
            title = "Notatka 1",
            content = "Treść notatki 1",
            date = LocalDate.of(2023, 1, 1)
        ),
        Note(
            courseId = 1,
            noteId = 2,
            title = "Notatka 2",
            content = "Treść notatki 2",
            date = LocalDate.of(2023, 2, 2)
        )
    )

    val course1Deadlines = listOf(
        Deadline(
            courseId = 1,
            deadlineId = 1,
            title = "Termin 1",
            description = "Opis terminu 1",
            date = LocalDate.of(2023, 1, 1)
        ),
        Deadline(
            courseId = 1,
            deadlineId = 2,
            title = "Termin 2",
            description = "Opis terminu 2",
            date = LocalDate.of(2023, 2, 2)
        ),
        Deadline(
            courseId = 1,
            deadlineId = 3,
            title = "Termin 3",
            description = "Opis terminu 3",
            date = LocalDate.of(2023, 3, 3)
        )
    )

    val course1Images = listOf(
        Image(
            courseId = 1,
            imageId = 1,
            filePath = ""
        )
    )

    val course1Dates = listOf(
        Date(
            courseId = 1,
            dateId = 1,
            date = LocalDate.of(2023, 1, 1),
            attendance = true,
            startTime = "10:00",
            endTime = "12:00"
        ),
        Date(
            courseId = 1,
            dateId = 2,
            date = LocalDate.of(2023, 2, 2),
            attendance = false,
            startTime = "14:00",
            endTime = "16:00"
        )
    )


    val course2 = Course(
        courseId = 2,
        name = "Polski",
        type = "P",
        building = "D.3",
        hall = "206",
        address = "ul. Piłsudskiego 13"
    )
    val course3 = Course(
        courseId = 3,
        name = "Angielski",
        type = "C",
        building = "D.4",
        hall = "209",
        address = "ul. Andrzeja 2"
    )
    val course2Notes = listOf(
        Note(
            courseId = 2,
            noteId = 3,
            title = "Notatka 3",
            content = "Treść notatki 3",
            date = LocalDate.of(2023, 3, 3)
        ),
        Note(
            courseId = 2,
            noteId = 4,
            title = "Notatka 4",
            content = "Treść notatki 4",
            date = LocalDate.of(2023, 4, 4)
        )
    )

    val course2Deadlines = listOf(
        Deadline(
            courseId = 2,
            deadlineId = 4,
            title = "Termin 4",
            description = "Opis terminu 4",
            date = LocalDate.of(2023, 3, 3)
        ),
        Deadline(
            courseId = 2,
            deadlineId = 5,
            title = "Termin 5",
            description = "Opis terminu 5",
            date = LocalDate.of(2023, 4, 4)
        )
    )

    val course2Images = listOf<Image>()

    val course2Dates = listOf(
        Date(
            courseId = 2,
            dateId = 3,
            date = LocalDate.of(2023, 3, 3),
            attendance = true,
            startTime = "08:00",
            endTime = "10:00"
        ),
        Date(
            courseId = 2,
            dateId = 4,
            date = LocalDate.of(2023, 4, 4),
            attendance = false,
            startTime = "12:00",
            endTime = "14:00"
        )
    )

    val course3Notes = listOf(
        Note(
            courseId = 3,
            noteId = 5,
            title = "Notatka 5",
            content = "Treść notatki 5",
            date = LocalDate.of(2023, 5, 5)
        )
    )

    val course3Deadlines = listOf<Deadline>()

    val course3Images = listOf(
        Image(
            courseId = 3,
            imageId = 4,
            filePath = "/path/to/image4.jpg"
        )
    )

    val course3Dates = listOf(
        Date(
            courseId = 3,
            dateId = 5,
            date = LocalDate.of(2023, 5, 5),
            attendance = true,
            startTime = "16:00",
            endTime = "18:00"
        )
    )

}

data class TestCourseData(
    val course: Course,
    val notes: List<Note>,
    val deadlines: List<Deadline>,
    val dates: List<Date>,
    val images: List<Image>
)