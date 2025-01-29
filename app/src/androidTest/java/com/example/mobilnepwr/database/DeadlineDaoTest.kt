package com.example.mobilnepwr.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.mobilnepwr.data.AppDatabase
import com.example.mobilnepwr.data.deadlines.Deadline
import com.example.mobilnepwr.data.deadlines.DeadlineDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class DeadlineDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var dao: DeadlineDao

    private val courses = TestData.courses
    private val deadlines = listOf(
        Deadline(
            deadlineId = 1,
            courseId = courses[0].courseId,
            title = "dd",
            description = "aa",
            date = LocalDate.of(2002, 1, 1)
        ),
        Deadline(
            deadlineId = 2,
            courseId = courses[1].courseId,
            title = "asd",
            description = "asd",
            date = LocalDate.of(2002, 1, 1)
        ),
        Deadline(
            deadlineId = 3,
            courseId = courses[1].courseId,
            title = "dd",
            description = "aa",
            date = LocalDate.of(2002, 1, 1)
        )
    )

    @Before
    fun setup() = runBlocking {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.deadlineDao()

        val courseDao = database.courseDao()
        courseDao.insertAll(courses)
    }

    @Test
    fun insertDeadline() = runBlocking {
        dao.insertDeadline(deadlines[0])
        val deadline = dao.getDeadline(deadlines[0].deadlineId).first()
        assertEquals(deadlines[0], deadline)

        dao.insertDeadline(deadlines[1])
        val deadline2 = dao.getDeadline(deadlines[1].deadlineId).first()
        assertEquals(deadlines[1], deadline2)

    }

    @Test
    fun updateDeadline() = runBlocking {
        insertDeadlines(deadlines)
        val updatedDeadline = deadlines[0].copy(title = "updated")
        dao.updateDeadline(updatedDeadline)
        val deadline = dao.getDeadline(deadlines[0].deadlineId).first()
        assertEquals(updatedDeadline, deadline)

    }

    @Test
    fun deleteDeadline() = runBlocking {
        insertDeadlines(deadlines)
        dao.deleteDeadline(deadlines[0])
        val result = dao.getAllDeadlines().first()
        assertEquals(2, result.size)
        assertEquals(null, dao.getDeadline(deadlines[0].deadlineId).first())

    }

    @Test
    fun getDeadlineByCourseId() = runBlocking {
        insertDeadlines(deadlines)
        var result = dao.getDeadlinesByCourseId(courses[0].courseId).first()
        assertEquals(result.size, 1)
        result = dao.getDeadlinesByCourseId(courses[1].courseId).first()
        assertEquals(result.size, 2)

        assertEquals(listOf<Deadline>(), dao.getDeadlinesByCourseId(294).first())

    }

    @Test
    fun getDeadline() = runBlocking {
        insertDeadlines(deadlines)
        val deadline = dao.getDeadline(deadlines[0].deadlineId).first()
        assertEquals(deadlines[0], deadline)

        val deadline2 = dao.getDeadline(deadlines[1].deadlineId).first()
        assertEquals(deadlines[1], deadline2)

    }

    @Test
    fun getAllDeadlines() = runBlocking {
        insertDeadlines(deadlines)
        val result = dao.getAllDeadlines().first()
        assertEquals(result.size, 3)
        assertEquals(result, dao.getAllDeadlines().first())

    }

    private suspend fun insertDeadlines(deadlines: List<Deadline>) {
        for (deadline in deadlines) {
            dao.insertDeadline(deadline)
        }
    }
}