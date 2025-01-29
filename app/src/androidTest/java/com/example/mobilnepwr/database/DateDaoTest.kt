package com.example.mobilnepwr.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mobilnepwr.data.AppDatabase
import com.example.mobilnepwr.data.dates.Date
import com.example.mobilnepwr.data.dates.DateDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class DateDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var dao: DateDao

    private val customCourses = TestData.courses
    private val customDates = listOf(
        Date(
            dateId = 1,
            date = LocalDate.of(2024, 1, 1),
            courseId = customCourses[0].courseId,
            startTime = "12:00",
            endTime = "14:00",
            attendance = false
        ),
        Date(
            dateId = 2,
            date = LocalDate.of(2024, 1, 2),
            courseId = customCourses[0].courseId,
            startTime = "12:00",
            endTime = "14:00",
            attendance = false
        ),
        Date(
            dateId = 3,
            date = LocalDate.of(2024, 1, 3),
            courseId = customCourses[1].courseId,
            startTime = "12:00",
            endTime = "14:00",
            attendance = false
        )
    )


    @Before
    fun setup() = runBlocking {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.dateDao()
        val courseDao = database.courseDao()

        courseDao.insertClass(customCourses[0])
        courseDao.insertClass(customCourses[1])

    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertDate() = runBlocking {
        dao.insertDate(customDates[0])

        val date1 = dao.getDate(customDates[0].dateId).first()

        assertEquals(customDates[0], date1)

        val course = database.courseDao().getAllClasses().first()

        dao.insertDate(customDates[2])

        val date2 = dao.getDate(customDates[2].dateId).first()

        assertEquals(customDates[2], date2)


    }

    @Test
    fun deleteDate() = runBlocking {
        for (date in customDates) {
            dao.insertDate(date)
        }
        dao.deleteDate(customDates[2])

        val dates = dao.getAllDates().first().sortedBy { it.dateId }
        assertEquals(2, dates.size)
        assertEquals(customDates[0], dates[0])
        assertEquals(customDates[1], dates[1])
    }

    @Test
    fun getGreaterThan() = runBlocking {
        for (date in customDates) {
            dao.insertDate(date)
        }
        val dates = dao.getGreaterThan(LocalDate.of(2024, 1, 2)).first()
        assertEquals(1, dates.size)
        assertEquals(0, dao.getGreaterThan(LocalDate.of(2024, 1, 3)).first().size)
    }

    @Test
    fun getLessThan() = runBlocking {
        for (date in customDates) {
            dao.insertDate(date)
        }
        val dates = dao.getLessThan(LocalDate.of(2024, 1, 2)).first()
        assertEquals(1, dates.size)
        assertEquals(2, dao.getLessThan(LocalDate.of(2024, 1, 3)).first().size)
        assertEquals(0, dao.getLessThan(LocalDate.of(2023, 12, 31)).first().size)
    }

    @Test
    fun updateDate() = runBlocking {
        for (date in customDates) {
            dao.insertDate(date)
        }
        val updatedDate = customDates[1].copy(attendance = true)
        dao.updateDate(updatedDate)

        assertEquals(dao.getDate(customDates[1].dateId).first(), updatedDate)
    }

    @Test
    fun getDatesByCourseId() = runBlocking {
        for (date in customDates) {
            dao.insertDate(date)
        }
        var dates = dao.getDatesByCourseId(customCourses[0].courseId).first()
        assertEquals(2, dates.size)
        assertEquals(customDates[0], dates[0])
        assertEquals(customDates[1], dates[1])
        dates = dao.getDatesByCourseId(customCourses[1].courseId).first()
        assertEquals(1, dates.size)
        assertEquals(customDates[2], dates[0])

    }

    @Test
    fun getDate() = runBlocking {
        for (date in customDates) {
            dao.insertDate(date)
        }
        val date = dao.getDate(customDates[0].dateId).first()
        assertEquals(customDates[0], date)

        val date2 = dao.getDate(customDates[1].dateId).first()
        assertEquals(customDates[1], date2)
    }

    @Test
    fun getAllDates() = runBlocking {
        for (date in customDates) {
            dao.insertDate(date)
        }
        val dates = dao.getAllDates().first().sortedBy { it.dateId }
        assertEquals(3, dates.size)
        assertEquals(customDates, dates)

    }
}