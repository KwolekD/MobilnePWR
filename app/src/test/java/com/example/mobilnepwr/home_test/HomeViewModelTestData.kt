package com.example.mobilnepwr.home_test

import com.example.mobilnepwr.data.courses.CourseWithDateDetails
import java.time.LocalDate

object HomeViewModelTestData {
    val day1: LocalDate = LocalDate.of(2025, 1, 6)
    val day2: LocalDate = day1.plusDays(1)
    val day3: LocalDate = day1.plusDays(2)
    val day4: LocalDate = day1.plusDays(3)
    val day5: LocalDate = day1.plusDays(4)
    val day6: LocalDate = day1.plusDays(5)
    val day7: LocalDate = day1.plusDays(6)

    val coursesForDay1 = listOf(
        CourseWithDateDetails(
            name = "Matematyka",
            type = "W",
            dateId = 1,
            courseId = 1,
            date = day1,
            attendance = true,
            startTime = "10:00",
            endTime = "12:00",
        ),
        CourseWithDateDetails(
            name = "Polski",
            type = "P",
            dateId = 2,
            courseId = 2,
            date = day1,
            attendance = false,
            startTime = "10:00",
            endTime = "12:00",
        ),
        CourseWithDateDetails(
            name = "Angielski",
            type = "C",
            dateId = 3,
            courseId = 3,
            date = day1,
            attendance = true,
            startTime = "10:00",
            endTime = "12:00",
        ),
    )

    val coursesForDay2 = listOf<CourseWithDateDetails>()
    val coursesForDay3 = listOf(
        CourseWithDateDetails(
            name = "Matematyka",
            type = "C",
            dateId = 4,
            courseId = 4,
            date = day3,
            attendance = true,
            startTime = "10:00",
            endTime = "12:00",
        ),
    )
    val coursesForDay4 = listOf(
        CourseWithDateDetails(
            name = "Matematyka",
            type = "P",
            dateId = 5,
            courseId = 5,
            date = day4,
            attendance = true,
            startTime = "10:00",
            endTime = "12:00",
        ),
        CourseWithDateDetails(
            name = "Fizyka",
            type = "W",
            dateId = 6,
            courseId = 6,
            date = day4,
            attendance = true,
            startTime = "10:00",
            endTime = "12:00",
        ),
    )

    val coursesForDay5 = listOf(
        CourseWithDateDetails(
            name = "Fizyka",
            type = "C",
            dateId = 7,
            courseId = 7,
            date = day5,
            attendance = true,
            startTime = "10:00",
            endTime = "12:00",
        ),
    )
    val coursesForDay6 = listOf<CourseWithDateDetails>()
    val coursesForDay7 = listOf<CourseWithDateDetails>()

    val coursesForPevWeek = listOf(
        CourseWithDateDetails(
            name = "Fizyka",
            type = "C",
            dateId = 8,
            courseId = 7,
            date = day5,
            attendance = true,
            startTime = "10:00",
            endTime = "12:00",
        )
    )

    val coursesForNextWeek = listOf(
        CourseWithDateDetails(
            name = "Matematyka",
            type = "C",
            dateId = 9,
            courseId = 7,
            date = day5,
            attendance = true,
            startTime = "10:00",
            endTime = "12:00",
        )
    )

}