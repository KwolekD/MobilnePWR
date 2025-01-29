package com.example.mobilnepwr.data

import biweekly.ICalendar
import biweekly.component.VEvent
import com.example.mobilnepwr.data.courses.Course
import com.example.mobilnepwr.data.courses.CourseDao
import com.example.mobilnepwr.data.courses.CourseRepository
import com.example.mobilnepwr.data.courses.CourseWithDateDetails
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class CourseRepositoryTest {
    private val courseDao = mockk<CourseDao>()
    private lateinit var courseRepository: CourseRepository
    private val courses = listOf(
        Course(
            courseId = 1,
            name = "Math",
            type = "Lecture",
            address = "Room 101",
            building = "Building A",
            hall = "Hall 1"
        ),
        Course(
            courseId = 2,
            name = "Physics",
            type = "Lecture",
            address = "Room 102",
            building = "Building B",
            hall = "Hall 2"
        )
    )

    @Before
    fun setUp() {
        coEvery { courseDao.clearDatabase() } just runs
        every { courseDao.getAllClasses() } returns mockk()

        courseRepository = CourseRepository(courseDao)

    }

    @Test
    fun `test getAllItemsStream should return flow of courses`() = runBlocking {
        // Given

        every { courseDao.getAllClasses() } returns flowOf(courses)

        // When
        courseRepository.getAllItemsStream().collect { result ->
            // Then
            assert(result == courses)
        }
    }

    @Test
    fun `test getItemStream should return flow of single course`() = runBlocking {
        // Given
        every { courseDao.getClassById(1) } returns flowOf(courses[1])

        // When
        courseRepository.getItemStream(1).collect { result ->
            // Then
            assert(result == courses[1])
        }
    }

    @Test
    fun `test insertItem should call courseDao insertClass`() = runBlocking {
        // Given
        coEvery { courseDao.insertClass(courses[1]) } just Runs

        // When
        courseRepository.insertItem(courses[1])

        // Then
        coVerify { courseDao.insertClass(courses[1]) }
    }

    @Test
    fun `test deleteItem should call courseDao delete`() = runTest {
        // Given
        coEvery { courseDao.delete(courses[0]) } just Runs

        // When
        courseRepository.deleteItem(courses[0])

        // Then
        coVerify { courseDao.delete(courses[0]) }
    }

    @Test
    fun `test updateItem should call courseDao update`() = runTest {
        coEvery { courseDao.update(courses[0]) } just Runs
        courseRepository.updateItem(courses[0])
        coVerify { courseDao.update(courses[0]) }
    }

    @Test
    fun `test clearDatabase should call courseDao clearDatabase`() = runTest {
        coEvery { courseDao.clearDatabase() } just Runs
        courseRepository.clearDatabase()
        coVerify { courseDao.clearDatabase() }
    }

    @Test
    fun `test getCoursesWithDateDetails should return flow of courses with date details`() =
        runBlocking {
            val date = LocalDate.of(2025, 1, 26)
            val courses_dates = courses.map {
                CourseWithDateDetails(
                    courseId = it.courseId,
                    name = it.name,
                    type = it.type,
                    dateId = 1,
                    date = date,
                    attendance = false,
                    startTime = "10:00",
                    endTime = "12:00"
                )
            }
            every { courseDao.getCoursesWithDateDetails(date) } returns flowOf(
                courses_dates
            )
            courseRepository.getCoursesWithDateDetails(date).collect { result ->
                assert(result == courses_dates)
            }
            coVerify { courseDao.getCoursesWithDateDetails(date) }
        }

    @Test
    fun `test importCoursesFromIcal should clear and insert courses`() = runTest {
        // Given
        val ical = mockk<ICalendar>()
        val event = mockk<VEvent>()
        val events = listOf(event)
        every { ical.events } returns events
        every { event.summary } returns mockk { every { value } returns "L - Aplikacje webowe na platformę .NET" }
        every { event.location?.value } returns "Plac Grunwaldzki 9\\, 50-377 Wrocław"
        every { event.description.value } returns "Sala: 333b\nBudynek dydaktyczny [D-2]\n\nhttps://web.usos.pwr.\n" +
                " edu.pl/kontroler.php?_action=katalog2/przedmioty/pokazZajecia&gr_nr=3&zaj_\n" +
                " cyk_id=72327\n"
        coEvery { courseDao.clearDatabase() } just Runs
        coEvery { courseDao.insertAll(any()) } just Runs

        // When
        courseRepository.importCoursesFromIcal(ical)

        // Then
        coVerify { courseDao.clearDatabase() }
        coVerify {
            courseDao.insertAll(
                listOf(
                    Course(
                        name = "Aplikacje webowe na platformę .NET",
                        type = "L",
                        address = "Plac Grunwaldzki 9\\, 50-377 Wrocław",
                        building = "Budynek dydaktyczny [D-2]",
                        hall = "333b",
                    )
                )
            )
        }
    }

    @Test
    fun `test getClassesAtDate should return flow of courses at specific date`() = runBlocking {
        // Given
        val date = LocalDate.of(2025, 1, 26)

        every { courseDao.getClassesAtDate(date) } returns flowOf(courses)

        // When
        courseRepository.getClassesAtDate(date).collect { result ->
            // Then
            assert(result == courses)
        }
    }
}