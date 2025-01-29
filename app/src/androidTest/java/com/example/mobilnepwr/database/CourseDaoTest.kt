package com.example.mobilnepwr.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.mobilnepwr.data.AppDatabase
import com.example.mobilnepwr.data.courses.CourseDao
import com.example.mobilnepwr.data.courses.CourseWithDateDetails
import com.example.mobilnepwr.data.dates.Date
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class CourseDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var dao: CourseDao

    private val customCourses = TestData.courses

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.courseDao()
    }

    @After
    fun teardown() {
        database.close()

    }

    @Test
    fun insertClass() = runBlocking {
        dao.insertClass(customCourses[0])

        assertEquals(customCourses[0], dao.getClassById(customCourses[0].courseId).first())
    }

    @Test
    fun insertAll() = runBlocking {
        dao.insertAll(customCourses)
        assertEquals(customCourses.size, dao.getAllClasses().first().size)
        assertEquals(customCourses, dao.getAllClasses().first().sortedBy { it.courseId })
    }

    @Test
    fun delete() = runBlocking {
        dao.insertAll(customCourses)
        dao.delete(customCourses[0])
        assertEquals(customCourses.size - 1, dao.getAllClasses().first().size)
        assertEquals(
            customCourses.subList(1, 3),
            dao.getAllClasses().first().sortedBy { it.courseId }
        )
    }

    @Test
    fun update() = runBlocking {
        dao.insertAll(customCourses)
        dao.update(customCourses[0].copy(name = "Physics"))
        assertEquals(
            customCourses[0].copy(name = "Physics"),
            dao.getClassById(customCourses[0].courseId).first()
        )


    }

    @Test
    fun getClassbyId() = runBlocking {
        dao.insertAll(customCourses)
        assertEquals(customCourses[0], dao.getClassById(customCourses[0].courseId).first())

        assertEquals(customCourses[1], dao.getClassById(customCourses[1].courseId).first())
    }

    @Test
    fun getAllClasses() = runBlocking {
        dao.insertAll(customCourses)
        assertEquals(customCourses, dao.getAllClasses().first().sortedBy { it.courseId })

    }

    @Test
    fun getCourseByNameAndType() = runBlocking {
        dao.insertAll(customCourses)
        assertEquals(
            customCourses[0],
            dao.getCourseByNameAndType(customCourses[0].name, customCourses[0].type).first()
        )

        assertEquals(
            customCourses[1],
            dao.getCourseByNameAndType(customCourses[1].name, customCourses[1].type).first()
        )

    }

    @Test
    fun getClassesAtDate() = runBlocking {
        dao.insertAll(customCourses)
        val date = LocalDate.of(2024, 1, 1)
        insertDates(date)

        assertEquals(
            customCourses.subList(0, 2),
            dao.getClassesAtDate(date).first().sortedBy { it.courseId }
        )

    }

    @Test
    fun getCoursesWithDateDetails() = runBlocking {
        dao.insertAll(customCourses)
        val date = LocalDate.of(2024, 1, 1)
        insertDates(date)
        val expected = listOf(
            CourseWithDateDetails(
                courseId = customCourses[0].courseId,
                name = customCourses[0].name,
                type = customCourses[0].type,
                dateId = 1,
                date = date,
                startTime = "12:00",
                endTime = "14:00",
                attendance = false
            ),
            CourseWithDateDetails(
                courseId = customCourses[1].courseId,
                name = customCourses[1].name,
                type = customCourses[1].type,
                dateId = 2,
                date = date,
                startTime = "12:00",
                endTime = "14:00",
                attendance = false
            )
        )
        assertEquals(expected, dao.getCoursesWithDateDetails(date).first())

    }

    @Test
    fun clearDatabase() = runBlocking {
        dao.insertAll(customCourses)
        val date = LocalDate.of(2024, 1, 1)
        insertDates(date)
        dao.clearDatabase()

        assertEquals(0, dao.getAllClasses().first().size)
        assertEquals(0, dao.getCoursesWithDateDetails(date).first().size)


    }


    private suspend fun insertDates(date: LocalDate) {
        val dateDao = database.dateDao()
        dateDao.insertDate(
            Date(
                dateId = 1,
                date = date,
                courseId = customCourses[0].courseId,
                startTime = "12:00",
                endTime = "14:00",
                attendance = false
            )
        )

        dateDao.insertDate(
            Date(
                dateId = 2,
                date = date,
                courseId = customCourses[1].courseId,
                startTime = "12:00",
                endTime = "14:00",
                attendance = false
            )
        )
    }
}