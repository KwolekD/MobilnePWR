package com.example.mobilnepwr.data

import com.example.mobilnepwr.data.deadlines.Deadline
import com.example.mobilnepwr.data.deadlines.DeadlineDao
import com.example.mobilnepwr.data.deadlines.DeadlineRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class DeadlineRepositoryTest {
    private val deadlineDao = mockk<DeadlineDao>()
    private lateinit var deadlineRepository: DeadlineRepository

    @Before
    fun setUp() {
        deadlineRepository = DeadlineRepository(deadlineDao)
    }

    @Test
    fun `getAllItemStream should return flow of deadlines`() = runTest {
        val deadlines = listOf(
            Deadline(
                deadlineId = 1,
                courseId = 1,
                title = "",
                description = "",
                date = LocalDate.of(2002, 1, 1)
            ),
            Deadline(
                deadlineId = 2,
                courseId = 2,
                title = "",
                description = "",
                date = LocalDate.of(2002, 1, 1)
            )
        )
        every { deadlineDao.getAllDeadlines() } returns flowOf(deadlines)

        deadlineRepository.getAllItemsStream().collect { result ->
            assert(result == deadlines)
        }
        verify {
            deadlineDao.getAllDeadlines()
        }

    }

    @Test
    fun `getItemStream should return flow of single deadline`() = runTest {
        val deadline = Deadline(
            deadlineId = 1,
            courseId = 1,
            title = "",
            description = "",
            date = LocalDate.of(2002, 1, 1)
        )
        every { deadlineDao.getDeadline(1) } returns flowOf(deadline)

        deadlineRepository.getItemStream(1).collect { result ->
            assert(result == deadline)
        }
        verify {
            deadlineDao.getDeadline(1)
        }
    }

    @Test
    fun `insertItem should call deadlineDao insert`() = runTest {
        val deadline = Deadline(
            deadlineId = 1,
            courseId = 1,
            title = "",
            description = "",
            date = LocalDate.of(2002, 1, 1)
        )
        coEvery { deadlineDao.insertDeadline(deadline) } returns Unit
        deadlineRepository.insertDeadline(deadline)
        coVerify {
            deadlineDao.insertDeadline(deadline)
        }
    }

    @Test
    fun `deleteDeadline should call deadlineDao delete`() = runTest {
        val deadline = Deadline(
            deadlineId = 1,
            courseId = 1,
            title = "",
            description = "",
            date = LocalDate.of(2002, 1, 1)
        )
        coEvery { deadlineDao.deleteDeadline(deadline) } returns Unit
        deadlineRepository.deleteDeadline(deadline)

        coVerify {
            deadlineDao.deleteDeadline(deadline)
        }

    }


    @Test
    fun `updateDeadline should call deadlineDao update`() = runTest {
        val deadline = Deadline(
            deadlineId = 1,
            courseId = 1,
            title = "",
            description = "",
            date = LocalDate.of(2002, 1, 1)
        )
        coEvery { deadlineDao.updateDeadline(deadline) } returns Unit

        deadlineRepository.updateDeadline(deadline)

        coVerify {
            deadlineDao.updateDeadline(deadline)
        }

    }

    @Test
    fun `getDeadlinesByCourseId should return flow of deadlines`() = runTest {
        val deadlines = listOf(
            Deadline(
                deadlineId = 1,
                courseId = 1,
                title = "",
                description = "",
                date = LocalDate.of(2002, 1, 1)
            )
        )

        every { deadlineDao.getDeadlinesByCourseId(1) } returns flowOf(deadlines)

        deadlineRepository.getDeadlinesByCourseId(1).collect { result ->
            assert(result == deadlines)
        }
        verify {
            deadlineDao.getDeadlinesByCourseId(1)
        }
    }

}