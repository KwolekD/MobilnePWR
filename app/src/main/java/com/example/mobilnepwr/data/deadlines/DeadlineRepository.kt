package com.example.mobilnepwr.data.deadlines


import kotlinx.coroutines.flow.Flow

class DeadlineRepository(val deadlineDao: DeadlineDao) {
    fun getAllItemsStream(): Flow<List<Deadline>> = deadlineDao.getAllDeadlines()

    fun getItemStream(id: Int): Flow<Deadline?> = deadlineDao.getDeadline(id)

    suspend fun insertItem(item: Deadline) = deadlineDao.insertDeadline(item)

    suspend fun deleteItem(item: Deadline) = deadlineDao.deleteDeadline(item)

    suspend fun updateItem(item: Deadline) = deadlineDao.updateDeadline(item)

    suspend fun getDeadlinesByCourseId(courseId: Int): Flow<List<Deadline>> =
        deadlineDao.getDeadlinesByCourseId(courseId)
}