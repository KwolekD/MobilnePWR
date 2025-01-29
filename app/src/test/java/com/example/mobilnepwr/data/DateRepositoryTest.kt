package com.example.mobilnepwr.data

import biweekly.ICalendar
import biweekly.component.VEvent
import biweekly.property.Summary
import biweekly.util.ICalDate
import com.example.mobilnepwr.data.courses.Course
import com.example.mobilnepwr.data.dates.Date
import com.example.mobilnepwr.data.dates.DateDao
import com.example.mobilnepwr.data.dates.DateRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

class DateRepositoryTest {
    private val dateDao = mockk<DateDao>()
    private lateinit var dateRepository: DateRepository

    @Before
    fun setUp() {
        dateRepository = DateRepository(dateDao)

    }

    @Test
    fun `insertDate should call dateDao insert`() = runTest {
        val date = Date(
            dateId = 1,
            courseId = 1,
            date = LocalDate.of(2025, 2, 3),
            startTime = "12:00",
            endTime = "13:00",
            attendance = true
        )
        coEvery { dateDao.insertDate(date) } returns Unit
        dateRepository.insertDate(date)
        coVerify {
            dateDao.insertDate(date)
        }

    }

    @Test
    fun `getDatesByCourseId should return flow of dates`() = runTest {
        val dates = listOf(
            Date(
                dateId = 1,
                courseId = 1,
                date = LocalDate.of(2002, 1, 1),
                startTime = "12:00",
                endTime = "13:00",
                attendance = true
            ),
            Date(
                dateId = 2,
                courseId = 1,
                date = LocalDate.of(2002, 1, 1),
                startTime = "12:00",
                endTime = "13:00",
                attendance = false
            )
        )
        coEvery { dateDao.getDatesByCourseId(1) } returns flowOf(dates)
        dateRepository.getDatesByCourseId(1)

        coVerify {
            dateDao.getDatesByCourseId(1)
        }

    }

    @Test
    fun `updateDate should call dateDao update`() = runTest {
        val date = Date(
            dateId = 1,
            courseId = 1,
            date = LocalDate.of(2, 1, 1),
            attendance = true,
            startTime = "12:00",
            endTime = "13:00"
        )
        coEvery { dateDao.updateDate(date) } returns Unit
        dateRepository.updateDate(date)

        coVerify {
            dateDao.updateDate(date)
        }
    }

    @Test
    fun `deleteDate should call dateDao delete`() = runTest {
        val date = Date(
            dateId = 1,
            courseId = 1,
            date = LocalDate.of(2022, 1, 1),
            startTime = "12:00",
            endTime = "13:00",
            attendance = true
        )
        coEvery { dateDao.deleteDate(date) } returns Unit
        dateRepository.deleteDate(date)
        coVerify {
            dateDao.deleteDate(date)
        }
    }

    @Test
    fun `getGreaterThan should return flow of dates`() = runTest {
        val dates = listOf(
            Date(
                dateId = 1,
                courseId = 1,
                date = LocalDate.of(2022, 1, 2),
                startTime = "12:00",
                endTime = "13:00",
                attendance = true
            ),
            Date(
                dateId = 2,
                courseId = 1,
                date = LocalDate.of(2022, 1, 2),
                startTime = "12:00",
                endTime = "13:00",
                attendance = false
            )
        )
        coEvery { dateDao.getGreaterThan(LocalDate.of(2022, 1, 1)) } returns flowOf(dates)

        dateRepository.getGreaterThan(LocalDate.of(2022, 1, 1))

        coVerify {
            dateDao.getGreaterThan(LocalDate.of(2022, 1, 1))
        }
    }

    @Test
    fun `getLessThan should return flow of dates`() = runTest {
        coEvery { dateDao.getLessThan(LocalDate.of(2022, 1, 1)) } returns flowOf(listOf())
        dateRepository.getLessThan(LocalDate.of(2022, 1, 1))
        coVerify {
            dateDao.getLessThan(LocalDate.of(2022, 1, 1))
        }


        @Test
        fun `importDatesFromIcal should call dateDao insert`() = runTest {
            val ical = mockk<ICalendar>()
            val event1 = mockk<VEvent>()
            val event2 = mockk<VEvent>()
            val events = listOf(event1, event2)

            val course1 = Course(
                courseId = 1,
                name = "Math",
                type = "L",
                address = "",
                building = "",
                hall = ""
            )
            val course2 = Course(
                courseId = 2,
                name = "Physics",
                type = "L",
                address = "",
                building = "",
                hall = ""
            )
            val courses = listOf(course1, course2)

            val summary1 = mockk<Summary> { every { value } returns "L - Math" }
            val summary2 = mockk<Summary> { every { value } returns "L - Physics" }

            every { ical.events } returns events
            every { event1.summary } returns summary1
            every { event2.summary } returns summary2
            val start1 = ICalDate.from(
                ZonedDateTime.of(2025, 2, 4, 10, 0, 0, 0, ZoneId.systemDefault()).toInstant()
            )
            val end1 = ICalDate.from(
                ZonedDateTime.of(2025, 1, 4, 12, 0, 0, 0, ZoneId.systemDefault()).toInstant()
            )
            every { event1.dateStart.value } returns ICalDate(start1)
            every { event1.dateEnd.value } returns ICalDate(end1)

            val start2 = ICalDate.from(
                ZonedDateTime.of(2025, 1, 3, 14, 0, 0, 0, ZoneId.systemDefault()).toInstant()
            )
            val end2 = ICalDate.from(
                ZonedDateTime.of(2025, 1, 3, 16, 0, 0, 0, ZoneId.systemDefault()).toInstant()
            )
            every { event2.dateStart.value } returns ICalDate(start2)
            every { event2.dateEnd.value } returns ICalDate(end2)

            // Mockowanie metody insertDate
            coEvery { dateRepository["insertDate"](any<Date>()) } returns ""

            // Wywołanie metody
            dateRepository.importDatesFromIcal(ical, courses)

            // Weryfikacja
            coVerify {
                dateRepository["insertDate"](withArg<Date> {
                    assert(it.courseId == 1)
                    assert(
                        it.date == start1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                    )
                    assert(it.startTime == "10:00")
                    assert(it.endTime == "12:00")
                    assert(it.attendance)
                })
                dateRepository["insertDate"](withArg<Date> {
                    assert(it.courseId == 2)
                    assert(
                        it.date == start2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                    )
                    assert(it.startTime == "14:00")
                    assert(it.endTime == "16:00")
                    assert(it.attendance)
                })
            }
        }


    }
}